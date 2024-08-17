package ly.david.musicsearch.ui.common.release

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.track.TrackListItem
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class TrackListItemTest : ScreenshotTest() {

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
                    recordingId = "r1",
                ),
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
                    formattedArtistCredits = "Some artist feat. Other artist",
                ),
            )
        }
    }
}
