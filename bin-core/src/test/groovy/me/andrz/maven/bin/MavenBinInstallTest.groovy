package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import org.eclipse.aether.resolution.ArtifactResolutionException
import org.junit.Test
import static org.junit.Assert.*

/**
 *
 */
@Slf4j
class MavenBinInstallTest extends MavenBinSettingsAbstractTest {

    @Test
    public void testEscapeDollars() {
        assertEquals('$gr \\$sh', MavenBinInstall.escapeDollars('$$gr $sh'))
        assertEquals('\\$gr', MavenBinInstall.escapeDollars('$gr'))
        assertEquals('$gr', MavenBinInstall.escapeDollars('$$gr'))
    }

    @Test
    public void test() {
        def artifact = 'org.apache.ant:ant'

        mavenBin.run(artifact, new MavenBinParams(
                install: true,
                run: false
        ))
    }

    @Test
    public void testWithoutSettings() {
        MavenBin mavenBin = new MavenBin()

        def artifact = 'org.apache.ant:ant'

        mavenBin.run(artifact, new MavenBinParams(
                install: true,
                run: false
        ))
    }

    @Test(expected = ArtifactResolutionException)
    public void testNonExistent() {
        def artifact = 'my.some.imaginary:package-thing'

        mavenBin.run(artifact, new MavenBinParams(
                install: true,
                run: false
        ))
    }

    @Test
    public void testAliasNull() {
        def artifact = 'org.apache.ant:ant'

        mavenBin.run(artifact, new MavenBinParams(
                install: true,
                run: false,
                alias: null
        ))
    }

}
