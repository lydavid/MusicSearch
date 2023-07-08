package ly.david.ui.common.recording

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.domain.listitem.RecordingListItemModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class RecordingListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            RecordingListItem(
                recording = RecordingListItemModel(
                    id = "1",
                    name = "Recording name",
                )
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            RecordingListItem(
                recording = RecordingListItemModel(
                    id = "2",
                    name = "Recording name",
                    firstReleaseDate = "2022-05-23",
                    disambiguation = "that one",
                    length = 25300000,
                    video = false,
                    formattedArtistCredits = "Some artist feat. Other artist"
                )
            )
        }
    }
}
