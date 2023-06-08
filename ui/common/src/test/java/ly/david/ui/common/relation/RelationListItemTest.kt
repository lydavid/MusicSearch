package ly.david.ui.common.relation

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class RelationListItemTest : PaparazziScreenshotTest() {

    @Test
    fun artist() {
        snapshot {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_0",
                    linkedResourceId = "2",
                    linkedResource = MusicBrainzResource.ARTIST,
                    label = "miscellaneous support",
                    name = "Artist Name",
                    disambiguation = "that guy",
                    attributes = "task: director & organizer, strings",
                )
            )
        }
    }

    @Test
    fun recording() {
        snapshot {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_1",
                    linkedResourceId = "2",
                    linkedResource = MusicBrainzResource.RECORDING,
                    label = "DJ-mixes",
                    name = "Recording Name",
                    additionalInfo = "by Artist Names (order: 10)",
                )
            )
        }
    }
}
