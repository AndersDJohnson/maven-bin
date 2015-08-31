package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import org.junit.Test;

/**
 *
 */
@Slf4j
public class MavenBinCliTest {

    @Test
    public void testMainSuccess() {
        exec('org.apache.ant:ant -h')
    }

    @Test
    public void testMainHelp() {
        exec('-help')
    }

    @Test
    public void testMain() {
        exec('org.apache.ant:ant')
    }

    @Test
    public void testInstall() {
        exec('-i org.apache.ant:ant')
    }

    @Test
    public void testInstallAlias() {
        exec('-i -a ant org.apache.ant:ant')
    }

    @Test
    public void testRun() {
        MavenBinCli.run('org.apache.ant:ant')
    }

    public void exec(String string) {
        try {
            MavenBinCli.main(arrayify(string))
        } catch (MavenBinExitValueNonZeroException e) {
        }
    }

    public String[] arrayify(String string) {
        return string.split(' ')
    }

}
