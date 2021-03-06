package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import me.andrz.maven.bin.env.EnvUtils
import me.andrz.maven.bin.util.MavenBinPathUtils
import org.eclipse.aether.resolution.ArtifactResolutionException
import org.junit.Ignore
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

        install(artifact)
    }

    @Test
    public void testWithoutSettings() {
        def artifact = 'org.apache.ant:ant'

        install(artifact)
    }

    @Test(expected = ArtifactResolutionException)
    public void testNonExistent() {
        def artifact = 'my.some.imaginary:package-thing'

        install(artifact)
    }

    @Test
    public void testAliasNull() {
        def artifact = 'org.apache.ant:ant'

        install(artifact, new MavenBinParams(
                alias: null
        ))
    }

    /**
     * This is not working yet - the artifact resolver is assuming a JAR.
     */
    @Ignore
    @Test
    public void testMavenPlugin() {
        def artifact = 'org.apache.maven.plugins:maven-dependency-plugin'

        install(artifact, new MavenBinParams(
                type: 'maven-plugin'
        ))
    }

    @Test
    public void testNotInPath() {
        EnvUtils.setenv('PATH', '')

        def artifact = 'org.apache.ant:ant'

        install(artifact)

        // TODO: Assert that we messaged to manually add to path.
    }

    @Test
    public void testEnvMavenBinPath() {
        String path = MavenBinPathUtils.tildeToUserHome('~/.tmp-mvbn')
        EnvUtils.setenv(MavenBinInstall.MVBN_PATH_ENV_VAR, path)

        def artifact = 'org.apache.ant:ant'

        install(artifact)

        // TODO: Assert that we installed to provided path.
    }

    private void install(String artifact, MavenBinParams params = null) {
        params = params ?: new MavenBinParams()

        params.install = true
        params.run = false

        mavenBin.run(artifact, params)
    }

}
