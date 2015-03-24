package me.andrz.maven.executable

import groovy.util.logging.Slf4j
import org.junit.Ignore
import org.junit.Test

/**
 *
 */
@Slf4j
class MavenExecutableTest {

    @Test
    public void test() {
        runWithOutput('org.apache.ant:ant:RELEASE')
    }

    @Test
    public void testNoOrg() {
        runWithOutput('org.apache.ant:ant')
    }

    @Ignore("requires repo settings")
    @Test
    public void testBintray() {
        runWithOutput('com.github.jengelman.gradle.plugins:shadow')
    }

    @Ignore("requires repo settings")
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


    private static void runWithOutput(String coords, MavenExecutableParams params = null) {
        def out = new StringBuilder()
        def err = new StringBuilder()
        Process proc = MavenExecutable.run(coords, params)
        proc.waitForProcessOutput(out, err)
        log.info out.toString()
        log.info err.toString()
    }

}
