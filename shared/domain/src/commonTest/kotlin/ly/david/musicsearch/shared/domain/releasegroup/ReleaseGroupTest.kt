package ly.david.musicsearch.shared.domain.releasegroup

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReleaseGroupTest {

    @Test
    fun `empty primary type and empty secondary types`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
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
        )
        assertEquals(
            "Album",
            releaseGroupTypes.getDisplayTypes(),
        )
    }

    @Test
    fun `primary type and a secondary type`() {
        val releaseGroupTypes = ReleaseGroupListItemModel(
            id = "",
            name = "",
            primaryType = "Album",
            secondaryTypes = persistentListOf(
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
            secondaryTypes = persistentListOf(
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
