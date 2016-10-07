package me.andrz.maven.bin.aether

import org.junit.Test

/**
 * Created by anders on 8/28/16.
 */
class MavenClasspathTest {

    @Test
    public void test() {
        String cp = MavenClasspath.getClasspathFromMavenProject(
                new File("/Users/anders/code/maven-bin/maven-bin-core/pom.xml"),
                null
        )
        println cp
    }
}
