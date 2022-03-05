package ly.david.mbjc.data

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

private const val ARTIST_1_NAME = "Artist Name"
private const val ARTIST_1_JOIN_PHRASE = "Artist Name"
private const val ARTIST_2_NAME = "Other Artist"

class ArtistCreditTest {

    private var artistCredits: List<ArtistCredit>? = null

    private val artistCredit1: ArtistCredit = mockk {
        every { name } returns ARTIST_1_NAME
        every { joinPhrase } returns ARTIST_1_JOIN_PHRASE
    }
    private val artistCredit2: ArtistCredit = mockk {
        every { name } returns ARTIST_2_NAME
        every { joinPhrase } returns ""
    }

    @Test
    fun `null artist credits`() {
        assertEquals("", artistCredits.getDisplayNames())
    }

    @Test
    fun `empty artist credits`() {
        artistCredits = listOf()
        assertEquals("", artistCredits.getDisplayNames())
    }

    @Test
    fun `single artist credit`() {
        artistCredits = listOf(artistCredit2)
        assertEquals(ARTIST_2_NAME, artistCredits.getDisplayNames())
    }

    @Test
    fun `multiple artist credit`() {
        artistCredits = listOf(artistCredit1, artistCredit2)
        assertEquals("$ARTIST_1_NAME$ARTIST_1_JOIN_PHRASE$ARTIST_2_NAME", artistCredits.getDisplayNames())
    }
}
