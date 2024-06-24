package ly.david.musicsearch.core.models.releasegroup

import ly.david.musicsearch.core.models.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.core.models.network.NO_TYPE
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReleaseGroupTest {

    @Test
    fun `null primary type and null secondary types`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = null,
            secondaryTypes = null,
        )
        assertEquals(
            NO_TYPE,
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `null primary type and empty secondary types`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = null,
            secondaryTypes = listOf(),
        )
        assertEquals(
            NO_TYPE,
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `empty primary type and empty secondary types`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = "",
            secondaryTypes = listOf(),
        )
        assertEquals(
            NO_TYPE,
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `primary type and empty secondary types`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = "Album",
            secondaryTypes = listOf(),
        )
        assertEquals(
            "Album",
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `primary type and null secondary types`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = "Album",
            secondaryTypes = null,
        )
        assertEquals(
            "Album",
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `null primary type and has a secondary type`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = null,
            secondaryTypes = listOf(
                "Compilation",
            ),
        )
        assertEquals(
            "Compilation",
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `primary type and a secondary type`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = "Album",
            secondaryTypes = listOf(
                "Compilation",
            ),
        )
        assertEquals(
            "Album + Compilation",
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `primary type and multiple secondary types`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = "Album",
            secondaryTypes = listOf(
                "Compilation",
                "Live",
                "Remix",
            ),
        )
        assertEquals(
            "Album + Compilation + Live + Remix",
            releaseGroupTypes.getDisplayTypes(),
        )
    }
}
