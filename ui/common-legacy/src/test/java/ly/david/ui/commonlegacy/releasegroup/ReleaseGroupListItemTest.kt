package ly.david.ui.commonlegacy.releasegroup

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.core.models.listitem.ReleaseGroupListItemModel
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ReleaseGroupListItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            ReleaseGroupListItem(
                releaseGroup = ReleaseGroupListItemModel(
                    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                    name = "欠けた心象、世のよすが",
                    primaryType = "EP",
                    firstReleaseDate = "2021-09-08",
                    formattedArtistCredits = "Some artist feat. some other artist",
                ),
            )
        }
    }
}
