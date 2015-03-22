package me.andrz.maven.executable

import com.jcabi.aether.Aether
import groovy.util.logging.Slf4j
import org.apache.maven.project.MavenProject
import org.sonatype.aether.artifact.Artifact
import org.sonatype.aether.repository.RemoteRepository
import org.sonatype.aether.util.artifact.DefaultArtifact
import org.sonatype.aether.util.artifact.JavaScopes

import java.util.jar.JarFile;

/**
 *
 */
@Slf4j
class MavenExecutable {

    public static final String defaultVersion = 'RELEASE'

    public static final File cwd = new File(System.getProperty("user.dir"))

    public static void main(String[] args) {
        log.debug args.toString()
        run(args[0])
    }

    public static Process run(String coords, String mainClassName = null) {
        return run(null, coords, mainClassName)
    }

    /**
     *
     * @param coords <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>
     */
    public static Process run(MavenProject project, String coords, String mainClassName = null) {

        if (! coords) {
            throw new RuntimeException("Must provide <artifact> argument.")
        }

        coords = sanitizeCoords(coords)

        Aether aether

        if (project) {
            aether = new Aether(project, getLocal())
        }
        else {
            aether = new Aether(getRemotes(), getLocal())
        }

        Artifact targetArtifact = new DefaultArtifact(coords)
        Collection<Artifact> artifacts = aether.resolve(targetArtifact, JavaScopes.RUNTIME)

        List<File> classpaths = []

        def targetJarFile = null

        artifacts.each { artifact ->
            if (artifactsMatch(targetArtifact, artifact)) {
                targetJarFile = artifact.file
            }
            classpaths.add(artifact.file)
        }

        log.debug "targetJarFile: $targetJarFile"

        if (! targetJarFile) {
            throw new RuntimeException("Could not resolve JAR for \"$coords\"")
        }

        if (! mainClassName) {
            mainClassName = getMainClassName(targetJarFile)
        }

        log.debug "main: $mainClassName"

        def classpath = classpaths.join(';')

        def env = System.getenv()
        def envStr = toEnvStrings(env)
        def command = "java -cp \"${classpath}\" ${mainClassName}"

        log.debug "command: ${command}"
        log.debug "envStr: ${envStr}"

        def proc = command.execute(envStr, cwd)

        return proc
    }

    /**
     * https://maven.apache.org/pom.html#Maven_Coordinates
     *
     * @param coords
     * @return
     */
    public static String sanitizeCoords(String coords) {
        if (coords.split(':').length < 3) {
            coords += ':' + defaultVersion
        }
        return coords
    }

    public static boolean artifactsMatch(Artifact a, Artifact b) {
        return a.artifactId == b.artifactId && a.groupId == b.groupId
    }

    public static String getMainClassName(File jarFile) {
        JarFile jf = new JarFile(jarFile);
        String mainClassName = jf?.manifest?.mainAttributes?.getValue("Main-Class")
        return mainClassName
    }

    public static String[] toEnvStrings(def env) {
        return env.collect { k, v -> "$k=$v" }
    }

    public static File getLocal() {
        Map<String, String> env = System.getenv();
        String m2home = env.get("M2_HOME");
        log.debug "M2_HOME: ${m2home}"

        return new File(m2home);
    }

    public static Collection<RemoteRepository> getRemotes() {
        return Arrays.asList(
                new RemoteRepository(
                        "maven-central",
                        "default",
                        "http://repo1.maven.org/maven2/"
                )
        );
    }

}
