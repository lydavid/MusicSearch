package ly.david.data

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

private const val ARTIST_1_NAME = "Artist Name"
private const val ARTIST_1_JOIN_PHRASE = " feat. "
private const val ARTIST_2_NAME = "Other Artist"

internal class ArtistCreditNameTest {

    private var artistCreditNames: List<ArtistCreditName>? = null

    private val artistCreditName1: ArtistCreditName = mockk {
        every { name } returns ARTIST_1_NAME
        every { joinPhrase } returns ARTIST_1_JOIN_PHRASE
    }
    private val artistCreditName2: ArtistCreditName = mockk {
        every { name } returns ARTIST_2_NAME
        every { joinPhrase } returns ""
    }
    private val artistCreditName3: ArtistCreditName = mockk {
        every { name } returns ARTIST_2_NAME
        every { joinPhrase } returns null
    }

    @Test
    fun `null artist credits`() {
        assertEquals("", artistCreditNames.getDisplayNames())
    }

    @Test
    fun `empty artist credits`() {
        artistCreditNames = listOf()
        assertEquals("", artistCreditNames.getDisplayNames())
    }

    @Test
    fun `single artist credit`() {
        artistCreditNames = listOf(artistCreditName2)
        assertEquals(ARTIST_2_NAME, artistCreditNames.getDisplayNames())
    }

    @Test
    fun `single artist credit with null join phrase`() {
        artistCreditNames = listOf(artistCreditName3)
        assertEquals(ARTIST_2_NAME, artistCreditNames.getDisplayNames())
    }

    @Test
    fun `multiple artist credit`() {
        artistCreditNames = listOf(artistCreditName1, artistCreditName2)
        assertEquals("$ARTIST_1_NAME$ARTIST_1_JOIN_PHRASE$ARTIST_2_NAME", artistCreditNames.getDisplayNames())
    }
}
