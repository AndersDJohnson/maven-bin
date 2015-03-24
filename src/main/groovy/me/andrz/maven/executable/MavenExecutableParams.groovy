package me.andrz.maven.executable

import org.eclipse.aether.artifact.Artifact

/**
 *
 */
class MavenExecutableParams {
    List<Artifact> artifacts
    Artifact targetArtifact
    String mainClassName
    String arguments
    String settingsPath
    String localRepoPath
}
