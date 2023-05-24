package ly.david.ui.common.listitem

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import ly.david.data.LifeSpan
import ly.david.ui.common.PaparazziWidgetScreenshotTest
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LifeSpanTextTest(
    config: DeviceConfig
) : PaparazziWidgetScreenshotTest(config) {

    @Test
    fun beginAndEnd() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    LifeSpanText(
                        lifeSpan = LifeSpan(
                            begin = "2022-12-15",
                            end = "2022-12-16"
                        )
                    )
                }
            }
        }
    }

    @Test
    fun sameBeginAndEnd() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    LifeSpanText(
                        lifeSpan = LifeSpan(
                            begin = "2022-12-15",
                            end = "2022-12-15"
                        )
                    )
                }
            }
        }
    }

    @Test
    fun beginOnly() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    LifeSpanText(
                        lifeSpan = LifeSpan(
                            begin = "2022-12-15",
                            end = null
                        )
                    )
                }
            }
        }
    }

    @Test
    fun endOnly() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    LifeSpanText(
                        lifeSpan = LifeSpan(
                            begin = null,
                            end = "2022-12-15"
                        )
                    )
                }
            }
        }
    }
}
