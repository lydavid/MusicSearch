package ly.david.ui.common.release

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.domain.listitem.TrackListItemModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class TrackListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            TrackListItem(
                track = TrackListItemModel(
                    id = "1",
                    title = "Track title",
                    position = 1,
                    number = "A",
                    mediumId = 1L,
                    recordingId = "r1"
                )
            )
        }
    }

    @Test
    fun long() {
        snapshot {
            TrackListItem(
                track = TrackListItemModel(
                    id = "2",
                    title = "Track title that is long and wraps",
                    position = 1,
                    number = "123",
                    length = 25300000,
                    mediumId = 2L,
                    recordingId = "r2",
                    formattedArtistCredits = "Some artist feat. Other artist"
                )
            )
        }
    }
}
