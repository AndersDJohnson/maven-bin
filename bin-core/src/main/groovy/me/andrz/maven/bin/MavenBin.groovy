package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import me.andrz.maven.bin.aether.Resolver
import me.andrz.maven.bin.env.EnvUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.SystemUtils
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.repository.RemoteRepository

import java.util.jar.JarFile;

/**
 *
 */
@Slf4j
class MavenBin {

    Resolver resolver

    public static final String defaultVersion = 'RELEASE'

    public static final File cwd = new File(System.getProperty("user.dir"))

    public init() {
        if (! resolver) {
            resolver = new Resolver()
        }
    }

    public static Artifact getArtifactFromCoords(String coords) {
        if (!coords) {
            throw new MavenBinArgumentException("Must provide <artifact> argument.")
        }
        coords = sanitizeCoords(coords)
        return new DefaultArtifact(coords)
    }

    public static String[] splitMainClassNameFromCoords(String coords) {
        if (!coords) {
            throw new MavenBinArgumentException("Must provide <artifact> argument.")
        }
        return coords.split('@')
    }

    public StdIo run(String coords, MavenBinParams params = null) {
        if (! params) params = new MavenBinParams()
        return run(null, coords, params)
    }

    /**
     *
     * @param coords <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>
     */
    public StdIo run(List<RemoteRepository> repositories, String coords, MavenBinParams params = null) {
        init()
        if (! params) params = new MavenBinParams()

        log.debug "params: $params"

        String mainClassName = null
        String[] split = splitMainClassNameFromCoords(coords)
        if (split.length == 2) {
            coords = split[0]
            mainClassName = split[1]
        }
        Artifact targetArtifact = getArtifactFromCoords(coords)
        List<Artifact> artifacts = resolver.resolves(repositories, targetArtifact)

        params.artifacts = artifacts
        params.targetArtifact = findResolvedArtifact(targetArtifact, artifacts)
        params.mainClassName = mainClassName

        return withArtifacts(params)
    }

    /**
     *
     * @param coords <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>
     */
    public StdIo withArtifacts(MavenBinParams params) {
        def stdIo = new StdIo()

        init()

        List<String> command = buildCommandList(params)

        if (params.getClasspath) {
            String classpath = getClasspath(params.artifacts)
            stdIo.out.append(classpath)
            return stdIo
        }

        if (params.install) {
            log.debug "installing"
            MavenBinParams installParams = params.clone()
            installParams.arguments = null
            MavenBinInstall.install(installParams)
        }
        else {
            log.debug "NOT installing"
        }

        if (params.run) {
            log.debug "command: ${command}"

            def env = EnvUtils.getenv()
            def envStr = EnvUtils.toEnvStrings(env)

            def proc = command.execute(envStr, cwd)
            proc.waitForProcessOutput(stdIo.out, stdIo.err)
            stdIo.exitValue = proc.exitValue()
        }

        return stdIo
    }

    public static String buildCommandString(MavenBinParams params) {
        def commandList = buildCommandList(params)
        // TODO: Need to consider quoting, etc.?
        return commandList.join(' ')
    }

    /**
     * Build command string for executing JAR specified by params.
     * Currently supports Windows 7 via Powershell.
     * TODO: Handle other OSs including Linux, Mac, etc.
     *
     * @param params
     * @return
     */
    public static List<String> buildCommandList(MavenBinParams params) {

        List<String> commandList = []

        List<Artifact> artifacts = params.artifacts
        Artifact targetArtifact = params.targetArtifact
        String mainClassName = params.mainClassName
        String arguments = params.arguments

        String classpath = getClasspath(artifacts)

        if (! targetArtifact) {
            throw new MavenBinArtifactException("No target artifact: ${targetArtifact}")
        }

        def targetJarFile = targetArtifact.file

        log.debug "targetJarFile: $targetJarFile"

        if (! targetJarFile) {
            throw new MavenBinArtifactException("Could not resolve JAR for target artifact: ${targetArtifact}")
        }

        if (! mainClassName) {
            mainClassName = getMainClassName(targetJarFile)
        }

        log.debug "main: $mainClassName"

        commandList.add('java')
        commandList.add('-classpath')
        commandList.add("\"${classpath}\"")
        commandList.add("${mainClassName}")

        if (arguments) {
            // TODO: Handle spaces within quoted args?
            def argsSplit = arguments.split(' ')
            for (def arg : argsSplit) {
                commandList.add(arg)
            }
        }

        return commandList
    }

    public static String getClasspath(List<Artifact> artifacts) {
        List<String> classpaths = getClasspaths(artifacts)
        // Separator is ";" on Windows, and ":" on Unix
        def pathSep = System.getProperty('path.separator')
        def classpath = classpaths.join(pathSep)
        return classpath
    }

    public static List<String> getClasspaths(List<Artifact> artifacts) {
        List<File> classpathFiles = getClasspathFiles(artifacts)
        List<String> classpaths = classpathFiles.collect { it.absolutePath }
        classpaths.add(SystemUtils.IS_OS_WINDOWS ? '%CLASSPATH%' : '${CLASSPATH}')
        return classpaths
    }

    public static List<File> getClasspathFiles(List<Artifact> artifacts) {
        List<File> classpaths = []
        for (Artifact artifact : artifacts) {
            classpaths.add(artifact.file)
        }
        return classpaths
    }

    /**
     * https://maven.apache.org/pom.html#Maven_Coordinates
     *
     * @param coords
     * @return
     */
    public static String sanitizeCoords(String coords) {
        if (StringUtils.countMatches(coords, ':') < 2) {
            coords += ':' + defaultVersion
        }
        return coords
    }

    /**
     * TODO: Support other fields (e.g. extension, classifier)?
     * See {@link org.eclipse.aether.artifact.DefaultArtifact#DefaultArtifact(java.lang.String, java.util.Map)}.
     *
     * @param artifact
     * @return
     */
    public static String makeCoords(Artifact artifact) {
        return artifact.groupId + ':' + artifact.artifactId + ':' + artifact.version
    }

    public static boolean artifactsMatch(Artifact a, Artifact b) {
        return a.artifactId == b.artifactId && a.groupId == b.groupId
    }

    public static Artifact findResolvedArtifact(Artifact artifact, List<Artifact> artifacts) {
        Artifact resolvedArtifact = null

        // find the resolved target artifact, and replace the parameter one
        for (Artifact otherArtifact : artifacts) {
            if (artifactsMatch(artifact, otherArtifact)) {
                resolvedArtifact = otherArtifact
                break
            }
        }

        if (! resolvedArtifact) {
            throw new MavenBinArtifactException("Could not match artifact to resolved artifact: " + resolvedArtifact)
        }

        return resolvedArtifact
    }

    public static String getMainClassName(File jarFile) {
        JarFile jf = new JarFile(jarFile);
        String mainClassName = jf?.manifest?.mainAttributes?.getValue("Main-Class")
        return mainClassName
    }

}
