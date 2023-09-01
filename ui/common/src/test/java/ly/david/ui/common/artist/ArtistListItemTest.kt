package ly.david.ui.common.artist

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.LifeSpanUiModel
import ly.david.data.domain.listitem.ArtistListItemModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ArtistListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            ArtistListItem(
                artist = ArtistListItemModel(
                    id = "1",
                    name = "artist name",
                    sortName = "sort name should not be seen",
                    countryCode = "CA"
                )
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            ArtistListItem(
                artist = ArtistListItemModel(
                    id = "2",
                    type = "Group, but for some reason it is really long and wraps around the screen",
                    name = "wow, this artist name is so long it will wrap around the screen",
                    sortName = "sort name should not be seen",
                    disambiguation = "blah, blah, blah, some really long text that forces wrapping",
                    countryCode = "XW",
                    lifeSpan = LifeSpanUiModel(
                        begin = "2020-12-31",
                        end = "2022-01-01"
                    )
                )
            )
        }
    }
}
