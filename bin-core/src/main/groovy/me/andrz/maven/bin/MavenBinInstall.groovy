package me.andrz.maven.bin

import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import org.eclipse.aether.artifact.Artifact

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

        Artifact targetArtifact = params.targetArtifact

        String command

        if (params.type == 'maven-plugin') {
            String coords = MavenBin.makeCoords(targetArtifact)
            command = 'mvn ' + coords + ':' // then immediately goal name as splat arg
        }
        else { // type == 'jar'
            command = MavenBin.buildCommandString(params) + ' ' // extra space for splat args
        }

        File binDir = initBinDir()

        def alias
        alias = params.alias ? params.alias : defaultAlias(targetArtifact)


        def cmdAlias = alias + ".cmd"
        def cmdAliasFile = new File(binDir, cmdAlias)

        log.debug "cmdAliasFile: $cmdAliasFile"

        URL cmdTemplateURL = this.getResource('templates/cmd.txt')
        String cmdTemplateText = cmdTemplateURL.text

        def engine = new SimpleTemplateEngine()
        def binding = [
                command: command
        ]
        def text = engine.createTemplate(cmdTemplateText).make(binding)

        cmdAliasFile.text = text
    }


    public static defaultAlias(Artifact artifact) {
        return artifact.artifactId + '--' + artifact.groupId + '--' + artifact.version
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
