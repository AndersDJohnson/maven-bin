package me.andrz.maven.bin

import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import org.eclipse.aether.artifact.Artifact

/**
 *
 */
@Slf4j
class MavenBinInstall {

    static installPath = System.getProperty("user.home") + File.separator + ".mvnx"


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
        File installFile = new File(installPath);

        log.debug("installFile: $installFile")

        installFile.mkdirs();

        String binPath = installPath + File.separator + "bin"

        File binFile = new File(binPath)

        binFile.mkdirs();

        addBinToPath(binPath)

        return binFile
    }


    /**
     * Currently supports Windows 7 via Powershell.
     * TODO: Handle other OSs including Linux, Mac, etc.
     * @param binPath
     */
    public static addBinToPath(String binPath) {
        addBinToPathForWindows(binPath)
    }


    public static addBinToPathForWindows(String binPath) {
        def env = System.getenv()
        def path = env.get("PATH")

        log.debug("path: $path")

        if (! path.contains(binPath)) {

            String setx = "setx PATH \"%PATH%;$binPath\""
            Process proc = setx.execute()

            // TODO: Capture output.
            proc.waitForProcessOutput()

            int exitValue = proc.exitValue()
            if (exitValue > 0) {
                throw new Exception("Error setting PATH. Exit code = $exitValue")
            }
        }
    }

}
