package me.andrz.maven.bin

import groovy.transform.AutoClone
import org.eclipse.aether.artifact.Artifact

/**
 *
 */
@AutoClone
class MavenBinParams {
    List<Artifact> artifacts
    Artifact targetArtifact
    String mainClassName
    String arguments
    String alias
    String type = 'jar'
    Boolean run = true
    Boolean install = false
    String settingsPath
    String localRepoPath
}
