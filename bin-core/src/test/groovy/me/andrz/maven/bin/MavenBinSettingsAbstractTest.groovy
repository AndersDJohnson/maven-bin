package me.andrz.maven.bin

import me.andrz.maven.bin.aether.Booter
import me.andrz.maven.bin.aether.Resolver
import org.junit.Before

/**
 *
 */
abstract class MavenBinSettingsAbstractTest {

    MavenBin mavenBin

    @Before
    public void before() {
        mavenBin = new MavenBin(
                resolver: new Resolver(
                        booter: new Booter(
                                settingsPath: MavenBin.class.getResource('a/settings.xml').path
                        )
                )
        )
    }

}
