package me.andrz.maven.bin.aether

import org.eclipse.aether.resolution.ArtifactResolutionException
import org.eclipse.aether.resolution.ArtifactResult

/**
 *
 */
class MavenBinArtifactResolutionException extends ArtifactResolutionException {

    public MavenBinArtifactResolutionException(String message, ArtifactResolutionException cause) {
        super(cause.getResults(), message, cause)
    }

    public MavenBinArtifactResolutionException(List<ArtifactResult> results) {
        super(results)
    }

    public MavenBinArtifactResolutionException(List<ArtifactResult> results, String message) {
        super(results, message)
    }

    public MavenBinArtifactResolutionException(List<ArtifactResult> results, String message, Throwable cause) {
        super(results, message, cause)
    }
}
