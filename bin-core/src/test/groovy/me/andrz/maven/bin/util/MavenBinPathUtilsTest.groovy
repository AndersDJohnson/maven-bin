package me.andrz.maven.bin.util

import com.jcabi.matchers.RegexMatchers
import org.junit.Test

import static org.hamcrest.MatcherAssert.assertThat

/**
 *
 */
class MavenBinPathUtilsTest {

    @Test
    public void testTildeToUserHome() {
        String actual = MavenBinPathUtils.tildeToUserHome('~/ok/yes')
        assertThat(actual, RegexMatchers.matchesPattern('^.*/.*/ok/yes$'))
    }
}
