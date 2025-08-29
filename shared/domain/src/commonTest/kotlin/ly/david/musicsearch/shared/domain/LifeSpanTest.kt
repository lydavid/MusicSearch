package ly.david.musicsearch.shared.domain

import kotlin.test.Test
import kotlin.test.assertEquals

internal class LifeSpanTest {

    @Test
    fun `null`() {
        val lifeSpan: LifeSpanUiModel? = null
        assertEquals("", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `null begin and end`() {
        val lifeSpan = LifeSpanUiModel()
        assertEquals("", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `empty begin and end`() {
        val lifeSpan = LifeSpanUiModel(begin = "", end = "")
        assertEquals("", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `has begin but no end`() {
        val lifeSpan = LifeSpanUiModel(begin = "2020-10-10", end = "")
        assertEquals("2020-10-10 -", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `has begin and end`() {
        val lifeSpan = LifeSpanUiModel(begin = "1957-03", end = "1970-04-10")
        assertEquals("1957-03 - 1970-04-10", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `same begin and end`() {
        val lifeSpan = LifeSpanUiModel(begin = "1957-03", end = "1957-03")
        assertEquals("1957-03", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `has end but no begin`() {
        val lifeSpan = LifeSpanUiModel(begin = "", end = "2021")
        assertEquals("?? - 2021", lifeSpan.getLifeSpanForDisplay())
    }
}
