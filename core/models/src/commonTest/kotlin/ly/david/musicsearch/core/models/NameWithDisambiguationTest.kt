package ly.david.musicsearch.core.models

import ly.david.musicsearch.core.models.listitem.ArtistListItemModel
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NameWithDisambiguationTest {

    @Test
    fun `empty name and disambiguation`() {
        val nameWithDisambiguation = ArtistListItemModel(
            id = "",
            name = "",
            sortName = "",
        )
        assertEquals("", nameWithDisambiguation.getNameWithDisambiguation())
    }

    @Test
    fun `name and empty disambiguation`() {
        val nameWithDisambiguation = ArtistListItemModel(
            id = "",
            name = "Some name",
            sortName = "",
        )
        assertEquals("Some name", nameWithDisambiguation.getNameWithDisambiguation())
    }

    // Non-sense, won't handle
    @Test
    fun `empty name and non-null disambiguation`() {
        val nameWithDisambiguation = ArtistListItemModel(
            id = "",
            name = "",
            sortName = "",
            disambiguation = "Disambiguation",
        )
        assertEquals(" (Disambiguation)", nameWithDisambiguation.getNameWithDisambiguation())
    }

    @Test
    fun `name and disambiguation`() {
        val nameWithDisambiguation = ArtistListItemModel(
            id = "",
            name = "Some name",
            sortName = "",
            disambiguation = "From Earth",
        )
        assertEquals("Some name (From Earth)", nameWithDisambiguation.getNameWithDisambiguation())
    }
}
