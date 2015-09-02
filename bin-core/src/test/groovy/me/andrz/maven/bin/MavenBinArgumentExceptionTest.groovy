package me.andrz.maven.bin

import org.junit.Test

/**
 * Created by anders on 9/1/15.
 */
class MavenBinArgumentExceptionTest {

    @Test
    @SuppressWarnings(['unused', 'GroovyResultOfObjectAllocationIgnored'])
    public void testConstructors() {
        new MavenBinArgumentException('ok')
        new MavenBinArgumentException('ok', new Throwable())
        new MavenBinArgumentException(new Throwable())
    }
}
