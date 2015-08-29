package me.andrz.maven.bin

import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import me.andrz.maven.bin.env.EnvPathUtils
import org.apache.commons.lang.SystemUtils
import org.eclipse.aether.artifact.Artifact

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermission

/**
 *
 */
@Slf4j
class MavenBinInstall {

    public static final String MVBN_PATH_ENV_VAR = "MVBN_PATH"
    private static final String DEFAULT_INSTALL_PATH = System.getProperty("user.home") + File.separator + ".mvbn"

    public static String getInstallPath() {
        def env = System.getenv()
        def MVBN_PATH = env.get(MVBN_PATH_ENV_VAR)
        if (MVBN_PATH) {
            return MVBN_PATH
        }
        else {
            return DEFAULT_INSTALL_PATH
        }
    }

    public static void install(MavenBinParams params) {
        File binDir = initBinDir()

        if (! checkInPath(binDir)) {
            println "Manually add to your path: \"$binDir\""
        }

        createAliasFile(binDir, params)
    }

    public static Boolean checkInPath(File file) {
        return EnvPathUtils.getPath().contains(file.absolutePath)
    }

    public static String defaultAlias(Artifact artifact) {
        return artifact.artifactId + '--' + artifact.groupId + '--' + artifact.version
    }

    public static String getAliasFileName(String alias) {
        def ext = SystemUtils.IS_OS_WINDOWS ? '.cmd' : ''
        return alias + ext
    }

    public static String getCommandTemplateForEnv() {
        return SystemUtils.IS_OS_WINDOWS ? 'templates/cmd.txt' : 'templates/sh.txt'
    }

    public static File createAliasFile(File parent, MavenBinParams params) {

        Artifact targetArtifact = params.targetArtifact

        String command

        if (params.type == 'maven-plugin') {
            String coords = MavenBin.makeCoords(targetArtifact)
            command = 'mvn ' + coords + ':' // then immediately goal name as splat arg
        }
        else { // type == 'jar'
            command = MavenBin.buildCommandString(params) + ' ' // extra space for splat args
        }

        String alias = params.alias ? params.alias : defaultAlias(targetArtifact)

        def cmdAlias = getAliasFileName(alias)
        def cmdAliasFile = new File(parent, cmdAlias)

        log.debug "cmdAliasFile: $cmdAliasFile"

        URL cmdTemplateURL = this.getResource(getCommandTemplateForEnv())
        String cmdTemplateText = cmdTemplateURL.text

        def escaped = escapeDollars(cmdTemplateText)

        def engine = new SimpleTemplateEngine()
        def binding = [
                command: command
        ]
        def text = engine.createTemplate(escaped).make(binding)

        cmdAliasFile.text = text

        applyPermissions(cmdAliasFile)

        println "Installed to \"$cmdAliasFile\"."
    }

    public static applyPermissions(File file) {

        Path path = file.toPath()

        try {
            file.setExecutable(true)
        }
        catch (SecurityException e) {
            log.warn("Exception applying file permissions.", e);
        }

        try {
            Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();

            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);

            Files.setPosixFilePermissions(path, perms);
        }
        catch (UnsupportedOperationException e) {
            log.warn("Exception applying file permissions.", e);
        }
    }

    public static String escapeDollars(String input) {
        String output = new String(input)
        output = output.replaceAll('\\$', '\\\\\\$')
        output = output.replaceAll('\\\\\\$\\\\\\$', '\\$')
        return output
    }

    public static File initBinDir() {
        def installPath = getInstallPath()
        log.debug("installPath: $installPath")
        File installFile = new File(installPath);
        installFile.mkdirs();

        String binPath = installPath + File.separator + "bin"
        log.debug("binPath: $binPath")
        File binFile = new File(binPath)
        binFile.mkdirs();

        return binFile
    }

}
