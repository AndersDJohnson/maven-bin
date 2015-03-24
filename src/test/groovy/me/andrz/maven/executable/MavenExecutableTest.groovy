package me.andrz.maven.executable

import groovy.util.logging.Slf4j
import me.andrz.maven.executable.aether.Booter
import me.andrz.maven.executable.aether.Resolver
import org.junit.Before
import org.junit.Test

/**
 *
 */
@Slf4j
class MavenExecutableTest {

    MavenExecutable mavenExecutable


    @Before
    public void before() {
        mavenExecutable = new MavenExecutable(
                resolver: new Resolver(
                        booter: new Booter(
                                settingsPath: MavenExecutable.class.getResource('a/settings.xml').path
                        )
                )
        )
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
        runWithOutput('org.apache.ant:ant', new MavenExecutableParams(
                arguments: '-h'
        ))
    }

    @Test
    public void testArgsJetty() {
        runWithOutput('org.eclipse.jetty:jetty-start', new MavenExecutableParams(
                arguments: '--help'
        ))
    }


    private void runWithOutput(String coords, MavenExecutableParams params = null) {
        def out = new StringBuilder()
        def err = new StringBuilder()
        Process proc = mavenExecutable.run(coords, params)
        proc.waitForProcessOutput(out, err)
        log.info out.toString()
        log.info err.toString()
    }

}
