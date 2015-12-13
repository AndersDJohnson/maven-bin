package me.andrz.maven.bin.aether

import org.eclipse.aether.resolution.ArtifactResult
import org.junit.Test

/**
 *
 */
class MavenBinArtifactResolutionExceptionTest {

    @SuppressWarnings(['unused', 'GroovyResultOfObjectAllocationIgnored'])
    @Test
    public void testConstructors() {
        List<ArtifactResult> results = []
        new MavenBinArtifactResolutionException(results)
        new MavenBinArtifactResolutionException(results, 'test')
        new MavenBinArtifactResolutionException(results, 'test', new Exception())
    }
}

