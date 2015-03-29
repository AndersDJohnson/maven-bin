package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import org.junit.Test

/**
 *
 */
@Slf4j
class MavenBinInstallTest {

    @Test
    public void test() {
        MavenBin mavenBin = new MavenBin()

        def artifact = 'org.apache.ant:ant'

        mavenBin.run(artifact, new MavenBinParams(
                install: true,
                run: false
        ))
    }

}
