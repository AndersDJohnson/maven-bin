package me.andrz.maven.bin

import org.junit.Test

/**
 * Created by anders on 9/1/15.
 */
class MavenBinArtifactExceptionTest {

    @Test
    @SuppressWarnings(['unused', 'GroovyResultOfObjectAllocationIgnored'])
    public void testConstructors() {
        new MavenBinArtifactException('ok')
        new MavenBinArtifactException('ok', new Throwable())
        new MavenBinArtifactException(new Throwable())
    }
}
