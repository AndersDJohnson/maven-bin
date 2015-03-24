package me.andrz.maven.executable.aether

import groovy.util.logging.Slf4j
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.apache.maven.settings.Profile
import org.apache.maven.settings.Repository
import org.apache.maven.settings.Settings
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest
import org.apache.maven.settings.building.SettingsBuilder
import org.apache.maven.settings.building.SettingsBuildingResult
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository

/**
 *
 */
@Slf4j
class Booter {

    String settingsPath
    String localRepoPath

    public RepositorySystem newRepositorySystem() {
        return ManualRepositorySystemFactory.newRepositorySystem();
    }

    public DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        String localRepoPath = getLocalRepoPath()

        LocalRepository localRepo = new LocalRepository(localRepoPath);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        return session;
    }

    public String getMavenHome() {
        def env = System.getenv()
        String mavenHome = env.get('M2_HOME')
        return mavenHome;
    }

    public String getUserMavenHome() {
        return System.getProperty('user.home') + File.separator + '.m2'
    }

    public File getLocalRepoPath() {
        if (! localRepoPath) {
            localRepoPath = getUserMavenHome() + File.separator + 'repository'
        }
        log.debug("Local repository: \"${localRepoPath}\"")
        return new File(localRepoPath)
    }

    public File getSettingsFile() {
        if (! settingsPath) {
            settingsPath = getUserMavenHome() + File.separator + 'settings.xml'
        }
        log.debug("Settings path: \"${settingsPath}\"")
        return new File(settingsPath)
    }

    public Settings getSettings() {
        SettingsBuilder settingsBuilder = new DefaultSettingsBuilderFactory().newInstance();
        DefaultSettingsBuildingRequest request = new DefaultSettingsBuildingRequest();
        request.setGlobalSettingsFile(getSettingsFile())
        SettingsBuildingResult result = settingsBuilder.build(request)
        Settings settings = result.getEffectiveSettings();
        return settings;
    }

    public List<RemoteRepository> newRepositories() {
        Settings settings = getSettings();

        List<RemoteRepository> remoteRepositories = new ArrayList<>()

        if (settings.profiles) {
            List<String> activeProfiles = settings.activeProfiles

            for (Profile profile : settings.profiles) {
                if (profile.id && activeProfiles.contains(profile.id)) {
                    if (profile.repositories) {
                        for (Repository repository : profile.repositories) {
                            remoteRepositories.add(toRemoteRepository(repository))
                        }
                    }
                }
            }
        }
        else {
            remoteRepositories.add(newCentralRepository())
        }
        return remoteRepositories
    }

    public RemoteRepository toRemoteRepository(Repository repository) {
        RemoteRepository.Builder remoteRepositoryBuilder = new RemoteRepository.Builder(
                repository.getId(),
                "default",
                repository.getUrl()
        )
//        remoteRepositoryBuilder.setReleasePolicy(repository.getReleases() as RepositoryPolicy)
        return remoteRepositoryBuilder.build()
    }

    public RemoteRepository newCentralRepository() {
        return new RemoteRepository.Builder("central", "default", "http://central.maven.org/maven2/").build()
    }

}
