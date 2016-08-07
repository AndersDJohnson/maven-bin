package me.andrz.maven.bin.util

import groovy.util.logging.Slf4j

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermission

/**
 *
 */
@Slf4j
class MavenBinFileUtils {

    public static boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix")

    public static void makeExecutable(File file) {

        Path path = file.toPath()

        // classic

        file.setExecutable(true)

        // posix

        if (!isPosix) return

        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();

        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        Files.setPosixFilePermissions(path, perms);
    }

    public static void tryMakeExecutable(File file) {
        try {
            makeExecutable(file)
        }
        catch (SecurityException e) {
            log.warn("Exception applying file permissions.", e);
        }
        catch (UnsupportedOperationException e) {
            log.warn("Exception applying file permissions.", e);
        }
    }

}
