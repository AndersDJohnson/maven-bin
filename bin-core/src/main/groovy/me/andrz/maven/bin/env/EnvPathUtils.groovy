package me.andrz.maven.bin.env

import groovy.util.logging.Slf4j
import org.apache.commons.lang3.SystemUtils

/**
 *
 */
@Slf4j
class EnvPathUtils {

    public static String getPathVarName() {
        return SystemUtils.IS_OS_WINDOWS ? 'Path' : 'PATH';
    }

    public static String getPathVar() {
        String pathVarName = getPathVarName()
        return SystemUtils.IS_OS_WINDOWS ? "%$pathVarName%" : "\${$pathVarName}";
    }

    public static String getPath() {
        return EnvUtils.getenv().get(getPathVarName())
//        String pathVar = getPathVar()
//        String echoCmd = getEchoCmd()
//        String echo = "$echoCmd $pathVar"
//        Process proc = echo.execute()
//
//        // TODO: Capture output.
//        StringBuilder out = new StringBuilder()
//        StringBuilder err = new StringBuilder()
//        proc.waitForProcessOutput(out, err)
//
//        int exitValue = proc.exitValue()
//        if (exitValue > 0) {
//            throw new Exception("Error getting PATH. Exit code = $exitValue")
//        }
//
//        return out
    }

//    public static String getEchoCmd() {
//        return SystemUtils.IS_OS_WINDOWS ? 'cmd /c echo' : 'echo';
//    }

    /**
     * Currently supports Windows 7 via Powershell.
     * TODO: Handle other OSs including Linux, Mac, etc.
     * @param binPath
     */
    @Deprecated
    public static void addBinToPath(String binPath) {
        addBinToPathForWindows(binPath)
    }

    @Deprecated
    public static void addBinToPathForWindows(String binPath) {

        def path = getPath()

        log.debug("path: $path")

        if (! path.contains(binPath)) {

            addToPathForWindows(binPath)
        }
    }

    @Deprecated
    public static void addToPathForWindows(String text) {
        String pathVar = getPathVar()
        String setx = "setx $pathVar \"%$pathVar%;$text\""
        Process proc = setx.execute()

        // TODO: Capture output.
        proc.waitForProcessOutput()

        int exitValue = proc.exitValue()
        if (exitValue > 0) {
            throw new Exception("Error setting PATH. Exit code = $exitValue")
        }
    }
}
