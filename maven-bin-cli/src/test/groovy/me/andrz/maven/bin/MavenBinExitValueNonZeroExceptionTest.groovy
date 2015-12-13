package me.andrz.maven.bin

import org.junit.Test

/**
 * Created by anders on 9/1/15.
 */
class MavenBinExitValueNonZeroExceptionTest {

    @Test
    @SuppressWarnings(['unused', 'GroovyResultOfObjectAllocationIgnored'])
    public void testConstructors() {
        new MavenBinExitValueNonZeroException('ok')
        new MavenBinExitValueNonZeroException('ok', 0)
        new MavenBinExitValueNonZeroException(0)
    }
}
