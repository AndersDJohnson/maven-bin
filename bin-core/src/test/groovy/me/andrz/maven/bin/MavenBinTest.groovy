package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import me.andrz.maven.bin.aether.MavenBinArtifactResolutionException
import org.junit.Ignore
import org.junit.Test

import static org.junit.Assert.*

/**
 *
 */
@Slf4j
class MavenBinTest extends MavenBinSettingsAbstractTest {

    @Ignore("meta install won't pass first test")
    @Test
    public void testMetaInstall() {
        runWithOutput('me.andrz.maven:bin', new MavenBinParams(
                alias: 'mvbn',
                type: 'maven-plugin'
        ))
    }

    @Test
    public void test() {
        runWithOutput('org.apache.ant:ant:RELEASE')
    }

    @Test(expected = MavenBinArgumentException)
    public void testNoCoords() {
        runWithOutput()
    }

    @Test(expected = MavenBinArtifactResolutionException)
    public void testNonExistent() {
        runWithOutput('my.some.imaginary:package-thing')
    }

    @Test
    public void testNoOrg() {
        runWithOutput('org.apache.ant:ant')
    }

    @Test
    public void testBintray() {
        runWithOutput('com.github.jengelman.gradle.plugins:shadow')
    }

    @Test
    public void testCustomBintray() {
        runWithOutput('me.andrz.jackson:jackson-json-reference')
    }

    @Test
    public void testArgs() {
        runWithOutput('org.apache.ant:ant', new MavenBinParams(
                arguments: '-h'
        ))
    }

    @Test
    public void testArgsJetty() {
        runWithOutput(
//                'org.eclipse.jetty:jetty-start',
//                'org.eclipse.jetty:jetty-start:(,9)', // Jetty 9 requires Java 8
                'org.eclipse.jetty:jetty-start:8.1.17.v20150415', // Jetty 9 requires Java 8
                new MavenBinParams(
                        arguments: '--help'
                )
        )
    }


    private Process runWithOutput(String coords = null, MavenBinParams params = null) {
        def out = new StringBuilder()
        def err = new StringBuilder()
        Process proc = mavenBin.run(coords, params)
        proc.waitForProcessOutput(out, err)
        log.info out.toString()
        log.info err.toString()
        return proc
    }

    private Process runWithOutputSuccess(String coords, MavenBinParams params = null) {
        Process proc = runWithOutput(coords, params)
        assertEquals(0, proc.exitValue())
        return proc
    }

}
