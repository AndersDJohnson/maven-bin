package me.andrz.maven.executable

import groovy.util.logging.Slf4j
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

    @Test
    public void testJCenter() {
        runWithOutput('com.github.jengelman.gradle.plugins:shadow:1.2.0')
    }


    private static void runWithOutput(String artifact) {
        def out = new StringBuilder()
        def err = new StringBuilder()
        Process proc = MavenExecutable.run(artifact)
        proc.waitForProcessOutput(out, err)
        log.info out.toString()
        log.info err.toString()
    }

}
