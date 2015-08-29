package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import me.andrz.maven.bin.aether.Booter
import me.andrz.maven.bin.aether.Resolver
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.not
import static org.junit.Assert.*

/**
 *
 */
@Slf4j
class MavenBinTest {

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
                'org.eclipse.jetty:jetty-start:(,9)', // Jetty 9 requires Java 8
                new MavenBinParams(
                        arguments: '--help'
                )
        )
    }


    private Process runWithOutput(String coords, MavenBinParams params = null) {
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
