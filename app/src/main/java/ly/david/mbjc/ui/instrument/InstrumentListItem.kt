package ly.david.mbjc.ui.instrument

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.domain.InstrumentListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

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
            Text(
                text = instrument.getNameWithDisambiguation(),
                style = TextStyles.getCardTitleTextStyle()
            )

            val type = instrument.type
            if (!type.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = type,
                    color = getSubTextColor(),
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }

            val description = instrument.description
            if (!description.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = description,
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        }
    }
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
