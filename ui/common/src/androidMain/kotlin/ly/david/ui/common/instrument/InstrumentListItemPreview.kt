package ly.david.ui.common.instrument

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

// Cannot be private.
internal class InstrumentCardPreviewParameterProvider : PreviewParameterProvider<InstrumentListItemModel> {
    override val values = sequenceOf(
        InstrumentListItemModel(
            id = "1",
            name = "Piano",
        ),
        InstrumentListItemModel(
            id = "2",
            name = "bass recorder",
            type = "Wind instrument",
        ),
        InstrumentListItemModel(
            id = "3",
            name = "Portuguese guitar",
            disambiguation = "Portugese plucked lute",
            type = "String instrument",
        ),
        InstrumentListItemModel(
            id = "4",
            name = "baroque guitar",
            disambiguation = "Baroque gut string guitar",
            type = "String instrument",
            description = "Predecessor of the modern classical guitar, it had gut strings and even gut frets. " +
                "First described in 1555, it surpassed the Renaissance lute's popularity.",
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(InstrumentCardPreviewParameterProvider::class) instrument: InstrumentListItemModel,
) {
    PreviewTheme {
        Surface {
            InstrumentListItem(instrument)
        }
    }
}
