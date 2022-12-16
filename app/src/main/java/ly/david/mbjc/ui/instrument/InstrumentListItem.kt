package ly.david.mbjc.ui.instrument

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.InstrumentListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.listitem.DisambiguationText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun InstrumentListItem(
    instrument: InstrumentListItemModel,
    onInstrumentClick: InstrumentListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onInstrumentClick(instrument) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {

            instrument.run {
                Text(
                    text = name,
                    style = TextStyles.getCardTitleTextStyle()
                )

                DisambiguationText(disambiguation = disambiguation)

                type.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }

                description.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }
            }
        }
    }
}

// Cannot be private.
@ExcludeFromJacocoGeneratedReport
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

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(InstrumentCardPreviewParameterProvider::class) instrument: InstrumentListItemModel
) {
    PreviewTheme {
        Surface {
            InstrumentListItem(instrument)
        }
    }
}
