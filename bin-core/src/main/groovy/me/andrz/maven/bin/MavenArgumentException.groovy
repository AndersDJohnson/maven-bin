package me.andrz.maven.bin

/**
 *
 */
class MavenArgumentException extends Exception {

    public MavenArgumentException(String message) {
        super(message)
    }

    public MavenArgumentException(String message, Throwable cause) {
        super(message, cause)
    }

    public MavenArgumentException(Throwable cause) {
        super(cause)
    }

}
