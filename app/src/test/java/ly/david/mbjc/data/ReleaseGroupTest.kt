package ly.david.mbjc.data

import io.mockk.every
import io.mockk.mockk
import ly.david.mbjc.data.network.NO_TYPE
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseGroupTest {

    private val releaseGroup: ReleaseGroup = mockk()

    @Test
    fun `null primary type, null secondary types`() {
        every { releaseGroup.primaryType } returns null
        every { releaseGroup.secondaryTypes } returns null
        assertEquals(NO_TYPE, releaseGroup.getDisplayTypes())
    }

    @Test
    fun `null primary type, empty secondary types`() {
        every { releaseGroup.primaryType } returns null
        every { releaseGroup.secondaryTypes } returns listOf()
        assertEquals(NO_TYPE, releaseGroup.getDisplayTypes())
    }

    @Test
    fun `empty primary type, empty secondary types`() {
        every { releaseGroup.primaryType } returns ""
        every { releaseGroup.secondaryTypes } returns listOf()
        assertEquals(NO_TYPE, releaseGroup.getDisplayTypes())
    }

    @Test
    fun `only has primary type`() {
        every { releaseGroup.primaryType } returns "Album"
        every { releaseGroup.secondaryTypes } returns null
        assertEquals("Album", releaseGroup.getDisplayTypes())
    }

    @Test
    fun `only has a secondary type`() {
        every { releaseGroup.primaryType } returns null
        every { releaseGroup.secondaryTypes } returns listOf("Compilation")
        assertEquals("Compilation", releaseGroup.getDisplayTypes())
    }

    @Test
    fun `primary type and a secondary type`() {
        every { releaseGroup.primaryType } returns "Album"
        every { releaseGroup.secondaryTypes } returns listOf("Compilation")
        assertEquals("Album + Compilation", releaseGroup.getDisplayTypes())
    }

    @Test
    fun `primary type and multiple secondary types`() {
        every { releaseGroup.primaryType } returns "Album"
        every { releaseGroup.secondaryTypes } returns listOf("Compilation", "Live", "Remix")
        assertEquals("Album + Compilation + Live + Remix", releaseGroup.getDisplayTypes())
    }
}
