package ly.david.musicsearch.ui.common.event

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewEventListItem() {
    PreviewTheme {
        Surface {
            EventListItem(
                event = EventListItemModel(
                    id = "e1",
                    name = "event name",
                    disambiguation = "that one",
                    type = "Concert",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewEventListItemLong() {
    PreviewTheme {
        Surface {
            EventListItem(
                event = EventListItemModel(
                    id = "05174e82-7716-444e-86a0-d0d1e1474662",
                    name = "1998-01-22: Sony Music Studios, New York City, NY, USA",
                    disambiguation = "“Where It’s At: The Rolling Stone State Of The Union”, " +
                        "a Rolling Stone Magazine 30th anniversary special",
                    type = null,
                    lifeSpan = LifeSpanUiModel(
                        begin = "1998-01-22",
                        end = "1998-01-22",
                        ended = true,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewEventListItemVisited() {
    PreviewTheme {
        Surface {
            EventListItem(
                event = EventListItemModel(
                    id = "05174e82-7716-444e-86a0-d0d1e1474662",
                    name = "1998-01-22: Sony Music Studios, New York City, NY, USA",
                    disambiguation = "“Where It’s At: The Rolling Stone State Of The Union”, " +
                        "a Rolling Stone Magazine 30th anniversary special",
                    type = null,
                    lifeSpan = LifeSpanUiModel(
                        begin = "1998-01-22",
                        end = "1998-01-22",
                        ended = true,
                    ),
                    visited = true,
                ),
            )
        }
    }
}
