package me.andrz.maven.bin.aether

import groovy.util.logging.Slf4j
import me.andrz.maven.bin.env.EnvUtils
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.apache.maven.settings.Profile
import org.apache.maven.settings.Proxy
import org.apache.maven.settings.Repository
import org.apache.maven.settings.Settings
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest
import org.apache.maven.settings.building.SettingsBuilder
import org.apache.maven.settings.building.SettingsBuildingResult
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.util.repository.AuthenticationBuilder
import org.eclipse.aether.util.repository.DefaultProxySelector
import org.eclipse.aether.repository.ProxySelector

/**
 *
 */
@Slf4j
class Booter {

    String settingsPath
    String localRepoPath

    public static List<RemoteRepository> defaultRepositories() {
        List<RemoteRepository> repositories
        RemoteRepository central = newCentralRepository()
        repositories = [ central ]
        return repositories
    }

    public static RepositorySystem newRepositorySystem() {
        return ManualRepositorySystemFactory.newRepositorySystem();
    }

    public DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        String localRepoPath = getLocalRepoPath()

        LocalRepository localRepo = new LocalRepository(localRepoPath);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        session.setProxySelector(getProxySelector())

        return session;
    }

    public static String getMavenHome() {
        String envMavenHome = getEnvMavenHome()
        if (envMavenHome) return envMavenHome
        return getUserMavenHome()
    }

    public static String getEnvMavenHome() {
        def env = EnvUtils.getenv()
        String mavenHome = env.get('M2_HOME')
        return mavenHome;
    }

    public static String getUserMavenHome() {
        String userMavenHome = System.getProperty('user.home') + File.separator + '.m2'
        return userMavenHome
    }

    public File getLocalRepoPath() {
        if (! localRepoPath) {
            localRepoPath = getMavenHome() + File.separator + 'repository'
        }
        log.debug("Local repository: \"${localRepoPath}\"")
        return new File(localRepoPath)
    }

    public File getSettingsFile() {
        if (! settingsPath) {
            settingsPath = getMavenHome() + File.separator + 'settings.xml'
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

    public List<RemoteRepository> newRepositories(RepositorySystemSession session) {
        Settings settings = getSettings();

        List<RemoteRepository> remoteRepositories = new ArrayList<>()

        if (settings.profiles) {
            List<String> activeProfiles = settings.activeProfiles

            for (Profile profile : settings.profiles) {
                if (profile.id && activeProfiles.contains(profile.id)) {
                    if (profile.repositories) {
                        for (Repository repository : profile.repositories) {
                            remoteRepositories.add(toRemoteRepository(repository, session))
                        }
                    }
                }
            }
        }
        if (!remoteRepositories) {
            remoteRepositories.addAll(defaultRepositories())
        }
        return remoteRepositories
    }

    public static RemoteRepository toRemoteRepository(Repository repository, RepositorySystemSession session) {

        // need a temp repo to lookup proxy
        RemoteRepository tempRemoteRepository = toRemoteRepositoryBuilder(repository).build()

        ProxySelector proxySelector = session.proxySelector
        org.eclipse.aether.repository.Proxy proxy = proxySelector.getProxy(tempRemoteRepository)

        // now build the actual repo and attach proxy
        RemoteRepository.Builder remoteRepositoryBuilder = toRemoteRepositoryBuilder(repository)
        remoteRepositoryBuilder.proxy = proxy
        RemoteRepository remoteRepository = remoteRepositoryBuilder.build()

        return remoteRepository
    }

    public static RemoteRepository.Builder toRemoteRepositoryBuilder(Repository repository) {
        RemoteRepository.Builder remoteRepositoryBuilder = new RemoteRepository.Builder(
                repository.getId(),
                "default",
                repository.getUrl()
        )
        return remoteRepositoryBuilder
//        remoteRepositoryBuilder.setReleasePolicy(repository.getReleases() as RepositoryPolicy)
    }

    public static RemoteRepository newCentralRepository() {
        return new RemoteRepository.Builder("central", "default", "http://central.maven.org/maven2/").build()
    }

    public ProxySelector getProxySelector()
    {
        DefaultProxySelector selector = new DefaultProxySelector();

        Settings settings = getSettings()

        if (settings.proxies) {
            for (Proxy proxy : settings.proxies) {
                String nonProxyHosts = proxy.nonProxyHosts
                selector.add( convertProxy(proxy), nonProxyHosts )
            }
        }

        return selector
    }

    public static org.eclipse.aether.repository.Proxy convertProxy(Proxy settingsProxy) {
        AuthenticationBuilder auth = new AuthenticationBuilder()
        auth
            .addUsername( settingsProxy.getUsername() )
            .addPassword( settingsProxy.getPassword() )
        org.eclipse.aether.repository.Proxy proxy = new org.eclipse.aether.repository.Proxy(
            settingsProxy.getProtocol(), settingsProxy.getHost(),
            settingsProxy.getPort(), auth.build()
        )
        return proxy
    }

}
