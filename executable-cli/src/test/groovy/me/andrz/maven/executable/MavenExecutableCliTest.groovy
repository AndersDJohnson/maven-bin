package me.andrz.maven.executable

import groovy.util.logging.Slf4j
import org.junit.Test;

/**
 *
 */
@Slf4j
public class MavenExecutableCliTest {

    @Test
    public void test() {
        MavenExecutableCli.run('org.apache.ant:ant')
    }

}
