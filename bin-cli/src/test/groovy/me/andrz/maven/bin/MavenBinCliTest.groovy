package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import org.junit.Test;

/**
 *
 */
@Slf4j
public class MavenBinCliTest {

    @Test
    public void test() {
        MavenBinCli.run('org.apache.ant:ant')
    }

}
