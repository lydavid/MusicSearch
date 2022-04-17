package ly.david.mbjc.data.domain

import io.mockk.every
import io.mockk.mockk
import ly.david.mbjc.data.LifeSpan
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ArtistUiModelTest {

    private val artistUiModel: ArtistUiModel = mockk()

    @Test
    fun `null lifespan`() {
        every { artistUiModel.lifeSpan } returns null
        assertEquals("", artistUiModel.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan, null begin and end`() {
        every { artistUiModel.lifeSpan } returns LifeSpan()
        assertEquals("", artistUiModel.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan, empty begin and end`() {
        every { artistUiModel.lifeSpan } returns LifeSpan(begin = "", end = "")
        assertEquals("", artistUiModel.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan begin, null end`() {
        every { artistUiModel.lifeSpan } returns LifeSpan(begin = "2020-10-10", end = null)
        assertEquals("2020-10-10", artistUiModel.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan begin, and end`() {
        every { artistUiModel.lifeSpan } returns LifeSpan(begin = "1957-03", end = "1970-04-10")
        assertEquals("1957-03 to 1970-04-10", artistUiModel.getLifeSpanForDisplay())
    }

    // Is valid?
    @Test
    fun `lifespan end, no begin`() {
        every { artistUiModel.lifeSpan } returns LifeSpan(begin = null, end = "2021")
        assertEquals(" to 2021", artistUiModel.getLifeSpanForDisplay())
    }
}
