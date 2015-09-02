package me.andrz.maven.bin

import me.andrz.maven.bin.aether.Booter
import me.andrz.maven.bin.aether.Resolver
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.junit.Before

/**
 *
 */
abstract class MavenBinSettingsAbstractTest {

    MavenBin mavenBin
    Artifact defaultArtifact
    String defaultCoords = 'org.apache.ant:ant'

    @Before
    public void before() {
        mavenBin = new MavenBin(
                resolver: new Resolver(
                        booter: new Booter(
                                settingsPath: MavenBin.class.getResource('a/settings.xml').path
                        )
                )
        )

        defaultArtifact = new DefaultArtifact(mavenBin.sanitizeCoords(defaultCoords))
    }

}
