package ly.david.musicsearch.ui.common.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.test.image.InitializeFakeImageLoader
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewEventListItem() {
    PreviewWithSharedElementTransition {
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

@PreviewLightDark
@Composable
internal fun PreviewEventListItemLong() {
    PreviewWithSharedElementTransition {
        EventListItem(
            event = EventListItemModel(
                id = "05174e82-7716-444e-86a0-d0d1e1474662",
                name = "1998-01-22: Sony Music Studios, New York City, NY, USA",
                disambiguation = "“Where It’s At: The Rolling Stone State Of The Union”, " +
                    "a Rolling Stone Magazine 30th anniversary special",
                lifeSpan = LifeSpanUiModel(
                    begin = "1998-01-22",
                    end = "1998-01-22",
                    ended = true,
                ),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewEventListItemVisited() {
    PreviewWithSharedElementTransition {
        EventListItem(
            event = EventListItemModel(
                id = "05174e82-7716-444e-86a0-d0d1e1474662",
                name = "1998-01-22: Sony Music Studios, New York City, NY, USA",
                disambiguation = "“Where It’s At: The Rolling Stone State Of The Union”, " +
                    "a Rolling Stone Magazine 30th anniversary special",
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

@PreviewLightDark
@Composable
internal fun PreviewEventListItemMultiDay() {
    PreviewWithSharedElementTransition {
        EventListItem(
            event = EventListItemModel(
                id = "0806a112-098e-49b5-a51c-34edf60c25d8",
                name = "Aimer 10th Anniversary Live in SAITAMA SUPER ARENA \"night world\"",
                type = "Stage performance",
                time = "17:00",
                cancelled = false,
                lifeSpan = LifeSpanUiModel(
                    begin = "2021-09-11",
                    end = "2021-09-12",
                ),
                visited = false,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewEventListItemCancelled() {
    PreviewWithSharedElementTransition {
        EventListItem(
            event = EventListItemModel(
                id = "b7c3f330-4fa8-4355-95de-af6e7c5d20b9",
                name = "1973-03-06: Vancouver Gardens, Vancouver, BC, Canada",
                type = "Concert",
                lifeSpan = LifeSpanUiModel(
                    begin = "1973-03-06",
                ),
                cancelled = true,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewEventListItemWithCoverArt() {
    InitializeFakeImageLoader()
    PreviewWithSharedElementTransition {
        EventListItem(
            event = EventListItemModel(
                id = "76a88474-912b-4a6e-b9e2-a98fd75ae51f",
                name = "コミックマーケット105",
                type = "Convention/Expo",
                lifeSpan = LifeSpanUiModel(
                    begin = "2024-12-29",
                    end = "2024-12-30",
                    ended = true,
                ),
                imageUrl = "https://www.example.com/image.jpg",
                imageId = ImageId(1L),
            ),
        )
    }
}
