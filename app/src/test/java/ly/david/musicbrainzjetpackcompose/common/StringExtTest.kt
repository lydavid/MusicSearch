package ly.david.musicbrainzjetpackcompose.common

import ly.david.musicbrainzjetpackcompose.ui.common.getYear
import ly.david.musicbrainzjetpackcompose.ui.common.useHttps
import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtTest {

    @Test
    fun `get year success`() {
        assertEquals("2021", "2021-09-08".getYear())
    }

    @Test
    fun `get year empty string`() {
        assertEquals("", "".getYear())
    }

    @Test
    fun `get year too short`() {
        assertEquals("", "202".getYear())
    }

    @Test
    fun `convert http to https`() {
        assertEquals(
            "https://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185.jpg",
            "http://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185.jpg".useHttps()
        )
    }
}
