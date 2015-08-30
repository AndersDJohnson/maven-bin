package me.andrz.maven.bin

import groovy.transform.AutoClone
import groovy.transform.ToString
import org.eclipse.aether.artifact.Artifact

/**
 *
 */
@ToString(includeNames=true)
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
