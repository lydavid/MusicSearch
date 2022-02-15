package ly.david.musicbrainzjetpackcompose.musicbrainz

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseGroupTest {

    private val releaseGroup: ReleaseGroup = mockk()

    @Test
    fun `get year success`() {
        every { releaseGroup.firstReleaseDate } returns "2021-09-08"
        assertEquals("2021", releaseGroup.getYear())
    }

    @Test
    fun `get year empty string`() {
        every { releaseGroup.firstReleaseDate } returns ""
        assertEquals("", releaseGroup.getYear())
    }

    @Test
    fun `get year too short`() {
        every { releaseGroup.firstReleaseDate } returns "202"
        assertEquals("", releaseGroup.getYear())
    }
}
