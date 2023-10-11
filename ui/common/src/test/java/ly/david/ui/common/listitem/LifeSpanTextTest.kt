package ly.david.ui.common.listitem

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class LifeSpanTextTest : ScreenshotTest() {

    @Test
    fun beginAndEnd() {
        snapshot {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-12-15",
                    end = "2022-12-16"
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
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
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
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
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
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
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
            )
        }
    }
}
