package me.andrz.maven.bin

/**
 * Created by anders on 10/24/15.
 */
class StdIo {
    Appendable out
    Appendable err
    int exitValue

    StdIo() {
        this.out = new StringBuffer()
        this.err = new StringBuffer()
        this.exitValue = 0
    }
}
