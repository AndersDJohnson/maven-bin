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

    public static String getPath() {
        return EnvUtils.getenv().get(getPathVarName())
    }

}
