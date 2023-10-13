package ly.david.musicsearch.domain.common

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.getLifeSpanForDisplay
import org.junit.Assert
import org.junit.Test

internal class LifeSpanTest {

    private var lifeSpan: LifeSpanUiModel? = null

    @Test
    fun `null lifespan`() {
        lifeSpan = null
        Assert.assertEquals("", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan, null begin and end`() {
        lifeSpan = LifeSpanUiModel()
        Assert.assertEquals("", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan, empty begin and end`() {
        lifeSpan = LifeSpanUiModel(begin = "", end = "")
        Assert.assertEquals("", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan begin, null end`() {
        lifeSpan = LifeSpanUiModel(begin = "2020-10-10", end = null)
        Assert.assertEquals("2020-10-10", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan begin, and end`() {
        lifeSpan = LifeSpanUiModel(begin = "1957-03", end = "1970-04-10")
        Assert.assertEquals("1957-03 to 1970-04-10", lifeSpan.getLifeSpanForDisplay())
    }

    @Test
    fun `lifespan same begin, and end`() {
        lifeSpan = LifeSpanUiModel(begin = "1957-03", end = "1957-03")
        Assert.assertEquals("1957-03", lifeSpan.getLifeSpanForDisplay())
    }

    // Is valid?
    @Test
    fun `lifespan end, no begin`() {
        lifeSpan = LifeSpanUiModel(begin = null, end = "2021")
        Assert.assertEquals(" to 2021", lifeSpan.getLifeSpanForDisplay())
    }
}
