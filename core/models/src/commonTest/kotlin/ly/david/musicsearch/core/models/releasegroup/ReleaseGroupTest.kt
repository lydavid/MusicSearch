package ly.david.musicsearch.core.models.releasegroup

import io.mockk.every
import io.mockk.mockk
import ly.david.musicsearch.core.models.network.NO_TYPE
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ReleaseGroupTest {

    private val releaseGroupTypes: ReleaseGroupTypes = mockk()

    @Test
    fun `null primary type, null secondary types`() {
        every { releaseGroupTypes.primaryType } returns null
        every { releaseGroupTypes.secondaryTypes } returns null
        assertEquals(NO_TYPE, releaseGroupTypes.getDisplayTypes())
    }

    @Test
    fun `null primary type, empty secondary types`() {
        every { releaseGroupTypes.primaryType } returns null
        every { releaseGroupTypes.secondaryTypes } returns listOf()
        assertEquals(NO_TYPE, releaseGroupTypes.getDisplayTypes())
    }

    @Test
    fun `empty primary type, empty secondary types`() {
        every { releaseGroupTypes.primaryType } returns ""
        every { releaseGroupTypes.secondaryTypes } returns listOf()
        assertEquals(NO_TYPE, releaseGroupTypes.getDisplayTypes())
    }

    @Test
    fun `primary type, empty secondary types`() {
        every { releaseGroupTypes.primaryType } returns "Album"
        every { releaseGroupTypes.secondaryTypes } returns listOf()
        assertEquals("Album", releaseGroupTypes.getDisplayTypes())
    }

    @Test
    fun `only has primary type`() {
        every { releaseGroupTypes.primaryType } returns "Album"
        every { releaseGroupTypes.secondaryTypes } returns null
        assertEquals("Album", releaseGroupTypes.getDisplayTypes())
    }

    @Test
    fun `only has a secondary type`() {
        every { releaseGroupTypes.primaryType } returns null
        every { releaseGroupTypes.secondaryTypes } returns listOf("Compilation")
        assertEquals("Compilation", releaseGroupTypes.getDisplayTypes())
    }

    @Test
    fun `primary type and a secondary type`() {
        every { releaseGroupTypes.primaryType } returns "Album"
        every { releaseGroupTypes.secondaryTypes } returns listOf("Compilation")
        assertEquals("Album + Compilation", releaseGroupTypes.getDisplayTypes())
    }

    @Test
    fun `primary type and multiple secondary types`() {
        every { releaseGroupTypes.primaryType } returns "Album"
        every { releaseGroupTypes.secondaryTypes } returns listOf("Compilation", "Live", "Remix")
        assertEquals("Album + Compilation + Live + Remix", releaseGroupTypes.getDisplayTypes())
    }
}
