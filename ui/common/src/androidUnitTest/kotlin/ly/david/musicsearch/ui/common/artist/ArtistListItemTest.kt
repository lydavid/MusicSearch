package ly.david.musicsearch.ui.common.artist

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ArtistListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            ArtistListItem(
                artist = ArtistListItemModel(
                    id = "1",
                    name = "artist name",
                    sortName = "sort name should not be seen",
                    countryCode = "CA",
                ),
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
                        end = "2022-01-01",
                    ),
                ),
            )
        }
    }
}
