package ly.david.ui.common.instrument

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.domain.listitem.InstrumentListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun InstrumentListItem(
    instrument: InstrumentListItemModel,
    modifier: Modifier = Modifier,
    onInstrumentClick: InstrumentListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Column {
                instrument.run {
                    Text(
                        text = name,
                        style = TextStyles.getCardBodyTextStyle()
                    )

                    DisambiguationText(disambiguation = disambiguation)

                    type.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }

                    description.ifNotNullOrEmpty {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                }
            }
        },
        modifier = modifier.clickable { onInstrumentClick(instrument) },
    )
}

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
            type = "Wind instrument"
        ),
        InstrumentListItemModel(
            id = "3",
            name = "Portuguese guitar",
            disambiguation = "Portugese plucked lute",
            type = "String instrument"
        ),
        InstrumentListItemModel(
            id = "4",
            name = "baroque guitar",
            disambiguation = "Baroque gut string guitar",
            type = "String instrument",
            description = "Predecessor of the modern classical guitar, it had gut strings and even gut frets. " +
                "First described in 1555, it surpassed the Renaissance lute's popularity."
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
