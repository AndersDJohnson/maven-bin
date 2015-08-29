package me.andrz.maven.bin

import groovy.util.logging.Slf4j
import org.junit.Test
import static org.junit.Assert.*

/**
 *
 */
@Slf4j
class MavenBinInstallTest {

    @Test
    public void testEscapeDollars() {
        assertEquals('$gr \\$sh', MavenBinInstall.escapeDollars('$$gr $sh'))
        assertEquals('\\$gr', MavenBinInstall.escapeDollars('$gr'))
        assertEquals('$gr', MavenBinInstall.escapeDollars('$$gr'))
    }

    @Test
    public void test() {
        MavenBin mavenBin = new MavenBin()

        def artifact = 'org.apache.ant:ant'

        mavenBin.run(artifact, new MavenBinParams(
                install: true,
                run: false
        ))
    }

    @Test
    public void testAliasNull() {
        MavenBin mavenBin = new MavenBin()

        def artifact = 'org.apache.ant:ant'

        mavenBin.run(artifact, new MavenBinParams(
                install: true,
                run: false,
                alias: null
        ))
    }

}
