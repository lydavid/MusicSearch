package ly.david.musicsearch.ui.common.label

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewLabelListItem() {
    PreviewWithTransitionAndOverlays {
        LabelListItem(
            label = LabelListItemModel(
                id = "1",
                name = "Music Label",
            ),
            filterText = "1",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLabelListItemWithCatalogNumberDoesNotShowDate() {
    PreviewWithTransitionAndOverlays {
        LabelListItem(
            label = LabelListItemModel(
                id = "5",
                name = "Sony Music",
                disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                type = "Original Production",
                labelCode = 10746,
                lifeSpan = LifeSpanUiModel(
                    begin = "1991-01-01",
                ),
                catalogNumbers = "CAT-123",
            ),
            filterText = "1",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLabelListItemWithBeginDate() {
    PreviewWithTransitionAndOverlays {
        LabelListItem(
            label = LabelListItemModel(
                id = "5",
                name = "Sony Music",
                disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                type = "Original Production",
                labelCode = 10746,
                lifeSpan = LifeSpanUiModel(
                    begin = "1991-01-01",
                ),
            ),
            filterText = "1",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLabelListItemVisited() {
    PreviewWithTransitionAndOverlays {
        LabelListItem(
            label = LabelListItemModel(
                id = "5",
                name = "Sony Music",
                disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                type = "Original Production",
                labelCode = 10746,
                catalogNumbers = "CAT-123",
                visited = true,
            ),
            filterText = "",
        )
    }
}
