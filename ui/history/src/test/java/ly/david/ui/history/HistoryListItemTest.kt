package ly.david.ui.history

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import java.util.Date
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryRoomModel
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class HistoryListItemTest : PaparazziScreenshotTest() {

    @Test
    fun releaseGroup() {
        snapshot {
            HistoryListItem(
                lookupHistory = LookupHistoryRoomModel(
                    title = "欠けた心象、世のよすが",
                    resource = MusicBrainzResource.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    numberOfVisits = 9999,
                    lastAccessed = Date(2023, 5, 2)
                )
            )
        }
    }

    @Test
    fun release() {
        snapshot {
            HistoryListItem(
                lookupHistory = LookupHistoryRoomModel(
                    title = "欠けた心象、世のよすが",
                    resource = MusicBrainzResource.RELEASE,
                    id = "165f6643-2edb-4795-9abe-26bd0533e59d",
                    lastAccessed = Date(2023, 5, 2)
                )
            )
        }
    }

    @Test
    fun artist() {
        snapshot {
            HistoryListItem(
                lookupHistory = LookupHistoryRoomModel(
                    title = "月詠み",
                    resource = MusicBrainzResource.ARTIST,
                    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                    lastAccessed = Date(2023, 5, 2)
                )
            )
        }
    }
}
