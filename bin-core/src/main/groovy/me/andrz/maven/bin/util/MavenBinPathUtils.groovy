package me.andrz.maven.bin.util

/**
 * Created by anders on 9/1/15.
 */
class MavenBinPathUtils {

    public static String tildeToUserHome(String path) {
        String userHome = System.getProperty("user.home")
        userHome = userHome.replace('\\','/')
        path = path.replaceFirst('^~', userHome)
        return path
    }

}
