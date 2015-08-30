package me.andrz.maven.bin.aether

import org.eclipse.aether.resolution.ArtifactResolutionException
import org.eclipse.aether.resolution.ArtifactResult

/**
 *
 */
class MavenBinArtifactResolutionException extends ArtifactResolutionException {

    MavenBinArtifactResolutionException(String message, ArtifactResolutionException cause) {
        super(cause.getResults(), message, cause)
    }

    MavenBinArtifactResolutionException(List<ArtifactResult> results) {
        super(results)
    }

    MavenBinArtifactResolutionException(List<ArtifactResult> results, String message) {
        super(results, message)
    }

    MavenBinArtifactResolutionException(List<ArtifactResult> results, String message, Throwable cause) {
        super(results, message, cause)
    }
}
