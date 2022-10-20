package ly.david.data.network

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class MediumMusicBrainzModelTest {

    private var media: List<MediumMusicBrainzModel>? = null

    private val cd1: MediumMusicBrainzModel = mockk {
        every { format } returns "CD"
        every { trackCount } returns 15
    }

    private val cd2: MediumMusicBrainzModel = mockk {
        every { format } returns "CD"
        every { trackCount } returns 8
    }

    private val bluRay: MediumMusicBrainzModel = mockk {
        every { format } returns "Blu-ray"
        every { trackCount } returns 24
    }

    @Test
    fun `null media`() {

        assertEquals(null, media.getFormatsForDisplay())
        assertEquals(null, media.getTracksForDisplay())
    }

    @Test
    fun `empty media`() {
        media = listOf()

        assertEquals(null, media.getFormatsForDisplay())
        assertEquals(null, media.getTracksForDisplay())
    }

    @Test
    fun `single CD`() {
        media = listOf(cd1)

        assertEquals("CD", media.getFormatsForDisplay())
        assertEquals("15", media.getTracksForDisplay())
    }

    @Test
    fun `2 CDs`() {
        media = listOf(cd1, cd2)

        assertEquals("2×CD", media.getFormatsForDisplay())
        assertEquals("15 + 8", media.getTracksForDisplay())
    }

    @Test
    fun `2 CDs, 1 Blu ray`() {
        media = listOf(cd1, cd2, bluRay)

        assertEquals("2×CD + Blu-ray", media.getFormatsForDisplay())
        assertEquals("15 + 8 + 24", media.getTracksForDisplay())
    }
}
