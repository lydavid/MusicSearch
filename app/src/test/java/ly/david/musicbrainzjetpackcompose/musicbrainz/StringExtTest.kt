package ly.david.musicbrainzjetpackcompose.musicbrainz

import ly.david.musicbrainzjetpackcompose.common.getYear
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
}
