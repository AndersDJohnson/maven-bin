package me.andrz.maven.bin

/**
 *
 */
class MavenBinExitValueNonZeroException extends Exception {

    public MavenBinExitValueNonZeroException(String message) {
        super(message)
    }

    public MavenBinExitValueNonZeroException(Integer exitValue) {
        this("Exit value non-zero.", exitValue)
    }

    public MavenBinExitValueNonZeroException(String message, Integer exitValue) {
        this("${message} exitValue=${exitValue}")
    }

}
