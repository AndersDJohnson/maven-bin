package me.andrz.maven.executable

import groovy.transform.AutoClone
import org.eclipse.aether.artifact.Artifact

/**
 *
 */
@AutoClone
class MavenExecutableParams {
    List<Artifact> artifacts
    Artifact targetArtifact
    String mainClassName
    String arguments
    String alias
    String type = 'jar'
    Boolean install
    String settingsPath
    String localRepoPath
}
