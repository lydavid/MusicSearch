package ly.david.ui.common.listitem

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.LifeSpanUiModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class LifeSpanTextTest : PaparazziScreenshotTest() {

    @Test
    fun beginAndEnd() {
        snapshot {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-12-15",
                    end = "2022-12-16"
                )
            )
        }
    }

    @Test
    fun sameBeginAndEnd() {
        snapshot {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-12-15",
                    end = "2022-12-15"
                )
            )
        }
    }

    @Test
    fun beginOnly() {
        snapshot {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-12-15",
                    end = null
                )
            )
        }
    }

    @Test
    fun endOnly() {
        snapshot {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = null,
                    end = "2022-12-15"
                )
            )
        }
    }
}
