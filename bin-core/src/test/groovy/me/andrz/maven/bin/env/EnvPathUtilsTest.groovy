package me.andrz.maven.bin.env

import org.junit.Ignore
import org.junit.Test

import static org.hamcrest.MatcherAssert.*
import static org.hamcrest.Matchers.*

/**
 *
 */
class EnvPathUtilsTest {

    /**
     * Attempted add to path and test updates, but can't seem to refresh.
     */
    @Ignore
    @Test
    public void testAddToPathForWindows() {
        EnvPathUtils.addBinToPath("--dummy--")
        // TODO: Refresh path?
//        def path = EnvPathUtils.getPath()
//        assertThat(path, containsString("--dummy--"))
    }

}
