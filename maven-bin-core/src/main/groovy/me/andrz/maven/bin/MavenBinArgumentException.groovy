package me.andrz.maven.bin

/**
 *
 */
class MavenBinArgumentException extends Exception {

    public MavenBinArgumentException(String message) {
        super(message)
    }

    public MavenBinArgumentException(String message, Throwable cause) {
        super(message, cause)
    }

    public MavenBinArgumentException(Throwable cause) {
        super(cause)
    }

}
