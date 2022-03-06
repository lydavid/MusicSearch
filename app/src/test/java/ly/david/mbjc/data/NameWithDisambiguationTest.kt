package ly.david.mbjc.data

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class NameWithDisambiguationTest {

    private val nameWithDisambiguation: NameWithDisambiguation = mockk()

    @Test
    fun `null name and disambiguation`() {
        every { nameWithDisambiguation.name } returns null
        every { nameWithDisambiguation.disambiguation } returns null
        assertEquals("", nameWithDisambiguation.getNameWithDisambiguation())
    }

    @Test
    fun `empty name and disambiguation`() {
        every { nameWithDisambiguation.name } returns ""
        every { nameWithDisambiguation.disambiguation } returns ""
        assertEquals("", nameWithDisambiguation.getNameWithDisambiguation())
    }

    @Test
    fun `name and empty disambiguation`() {
        every { nameWithDisambiguation.name } returns "Some name"
        every { nameWithDisambiguation.disambiguation } returns ""
        assertEquals("Some name", nameWithDisambiguation.getNameWithDisambiguation())
    }

    @Test
    fun `name and null disambiguation`() {
        every { nameWithDisambiguation.name } returns "Some name"
        every { nameWithDisambiguation.disambiguation } returns null
        assertEquals("Some name", nameWithDisambiguation.getNameWithDisambiguation())
    }

    // Non-sense, won't handle
    @Test
    fun `null name and non-null disambiguation`() {
        every { nameWithDisambiguation.name } returns null
        every { nameWithDisambiguation.disambiguation } returns "Disambiguation"
        assertEquals(" (Disambiguation)", nameWithDisambiguation.getNameWithDisambiguation())
    }

    // Non-sense, won't handle
    @Test
    fun `empty name and non-null disambiguation`() {
        every { nameWithDisambiguation.name } returns ""
        every { nameWithDisambiguation.disambiguation } returns "Disambiguation"
        assertEquals(" (Disambiguation)", nameWithDisambiguation.getNameWithDisambiguation())
    }

    @Test
    fun `name and disambiguation`() {
        every { nameWithDisambiguation.name } returns "Some name"
        every { nameWithDisambiguation.disambiguation } returns "From Earth"
        assertEquals("Some name (From Earth)", nameWithDisambiguation.getNameWithDisambiguation())
    }
}
