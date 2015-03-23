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

    @Ignore("not working")
    @Test
    public void testBintray() {
        runWithOutput('com.github.jengelman.gradle.plugins:shadow:1.2.0')
//        runWithOutput('com.github.jengelman.gradle.plugins:shadow')
    }

    @Ignore("not working")
    @Test
    public void testCustomBintray() {
        runWithOutput('me.andrz.jackson:jackson-json-reference:0.1.0')
//        runWithOutput('me.andrz.jackson:jackson-json-reference')
    }


    private static void runWithOutput(String coords) {
        def out = new StringBuilder()
        def err = new StringBuilder()
        Process proc = MavenExecutable.run(coords)
        proc.waitForProcessOutput(out, err)
        log.info out.toString()
        log.info err.toString()
    }

}
