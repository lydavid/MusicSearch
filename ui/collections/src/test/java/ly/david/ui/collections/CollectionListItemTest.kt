package ly.david.ui.collections

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CollectionListItemTest(
    config: DeviceConfig
) : PaparazziWidgetScreenshotTest(config) {

    @Test
    fun remote() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    CollectionListItem(
                        collection = CollectionListItemModel(
                            id = "3048448c-0605-494a-9e9f-c1a0521906f1",
                            isRemote = true,
                            name = "My collection with a very long title",
                            description = "Some songs",
                            entity = MusicBrainzResource.RECORDING,
                            entityCount = 9999,
                            entityIds = listOf(
                                "1b1e4b65-9b1a-48cd-8e3a-b4824f15bf0c",
                                "b437fbda-9c32-4078-afa2-1afb98ff0d74"
                            )
                        )
                    )
                }
            }
        }
    }

    @Test
    fun local() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    CollectionListItem(
                        collection = CollectionListItemModel(
                            id = "3048448c-0605-494a-9e9f-c1a0521906f1",
                            isRemote = false,
                            name = "My collection with a very long title",
                            description = "Some songs",
                            entity = MusicBrainzResource.RECORDING,
                            entityCount = 9999,
                            entityIds = listOf(
                                "1b1e4b65-9b1a-48cd-8e3a-b4824f15bf0c",
                                "b437fbda-9c32-4078-afa2-1afb98ff0d74"
                            )
                        )
                    )
                }
            }
        }
    }
}
