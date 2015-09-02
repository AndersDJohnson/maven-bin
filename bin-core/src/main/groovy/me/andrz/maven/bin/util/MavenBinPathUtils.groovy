package me.andrz.maven.bin.util

/**
 * Created by anders on 9/1/15.
 */
class MavenBinPathUtils {

    public static String tildeToUserHome(String path) {
        return path.replaceFirst('^~', System.getProperty("user.home"))
    }

}
