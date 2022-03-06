package ly.david.mbjc.data

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class NameWithDisambiguationTest {

    private val nameWithDisambiguation: NameWithDisambiguation = mockk()

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
    fun `name and disambiguation`() {
        every { nameWithDisambiguation.name } returns "Some name"
        every { nameWithDisambiguation.disambiguation } returns "From Earth"
        assertEquals("Some name (From Earth)", nameWithDisambiguation.getNameWithDisambiguation())
    }
}
