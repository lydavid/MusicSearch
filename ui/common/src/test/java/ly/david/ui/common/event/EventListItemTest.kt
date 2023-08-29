package ly.david.ui.common.event

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.LifeSpanMusicBrainzModel
import ly.david.data.domain.listitem.EventListItemModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class EventListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            EventListItem(
                event = EventListItemModel(
                    id = "e1",
                    name = "event name",
                    disambiguation = "that one",
                    type = "Concert",
                )
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            EventListItem(
                event = EventListItemModel(
                    id = "05174e82-7716-444e-86a0-d0d1e1474662",
                    name = "1998-01-22: Sony Music Studios, New York City, NY, USA",
                    disambiguation = "“Where It’s At: The Rolling Stone State Of The Union”, " +
                        "a Rolling Stone Magazine 30th anniversary special",
                    type = null,
                    lifeSpan = LifeSpanMusicBrainzModel(
                        begin = "1998-01-22",
                        end = "1998-01-22",
                        ended = true
                    )
                )
            )
        }
    }
}
