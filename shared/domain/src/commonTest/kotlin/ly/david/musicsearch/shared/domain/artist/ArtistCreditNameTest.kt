package ly.david.musicsearch.shared.domain.artist

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ArtistCreditNameTest {

    @Test
    fun `null artist credits`() {
        val artistCreditNames: List<ArtistCreditUiModel>? = null
        assertEquals(
            "",
            artistCreditNames.getDisplayNames(),
        )
    }

    @Test
    fun `empty artist credits`() {
        val artistCreditNames: List<ArtistCreditUiModel> = listOf()
        assertEquals(
            "",
            artistCreditNames.getDisplayNames(),
        )
    }

    @Test
    fun `single artist credit`() {
        val artistCreditNames = listOf(
            ArtistCreditUiModel(
                artistId = "",
                name = "Artist Name",
                joinPhrase = "",
            ),
        )
        assertEquals(
            "Artist Name",
            artistCreditNames.getDisplayNames(),
        )
    }

    @Test
    fun `single artist credit with null join phrase`() {
        val artistCreditNames = listOf(
            ArtistCreditUiModel(
                artistId = "",
                name = "Artist Name",
                joinPhrase = null,
            ),
        )
        assertEquals(
            "Artist Name",
            artistCreditNames.getDisplayNames(),
        )
    }

    @Test
    fun `multiple artist credit`() {
        val artistCreditNames = listOf(
            ArtistCreditUiModel(
                artistId = "",
                name = "Artist Name",
                joinPhrase = " feat. ",
            ),
            ArtistCreditUiModel(
                artistId = "",
                name = "Other Artist",
                joinPhrase = "",
            ),
        )
        assertEquals(
            "Artist Name feat. Other Artist",
            artistCreditNames.getDisplayNames(),
        )
    }
}
