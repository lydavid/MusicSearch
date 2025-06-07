package ly.david.musicsearch.shared.feature.details.instrument

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewInstrumentDetailsUi() {
    PreviewWithSharedElementTransition {
        InstrumentDetailsTabUi(
            instrument = InstrumentDetailsModel(
                id = "i1",
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
