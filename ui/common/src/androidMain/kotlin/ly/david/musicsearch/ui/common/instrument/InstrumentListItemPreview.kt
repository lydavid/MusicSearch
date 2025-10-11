package ly.david.musicsearch.ui.common.instrument

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewInstrumentListItemModel() {
    PreviewWithTransitionAndOverlays {
        InstrumentListItem(
            instrument = InstrumentListItemModel(
                id = "b3eac5f9-7859-4416-ac39-7154e2e8d348",
                name = "Piano",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewInstrumentListItemModelAllInfo() {
    PreviewWithTransitionAndOverlays {
        InstrumentListItem(
            instrument = InstrumentListItemModel(
                id = "08450be5-f6d2-46d6-8be0-67087c02162c",
                name = "baroque guitar",
                disambiguation = "Baroque gut string guitar",
                type = "String instrument",
                description = "Predecessor of the modern classical guitar, " +
                    "it had gut strings and even gut frets. " +
                    "First described in 1555, it surpassed the Renaissance lute's popularity.",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewInstrumentListItemModelVisited() {
    PreviewWithTransitionAndOverlays {
        InstrumentListItem(
            instrument = InstrumentListItemModel(
                id = "08450be5-f6d2-46d6-8be0-67087c02162c",
                name = "baroque guitar",
                disambiguation = "Baroque gut string guitar",
                type = "String instrument",
                description = "Predecessor of the modern classical guitar, " +
                    "it had gut strings and even gut frets. " +
                    "First described in 1555, it surpassed the Renaissance lute's popularity.",
                visited = true,
            ),
        )
    }
}
