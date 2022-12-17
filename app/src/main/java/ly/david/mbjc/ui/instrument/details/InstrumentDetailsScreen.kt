package ly.david.mbjc.ui.instrument.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.InstrumentListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun InstrumentDetailsScreen(
    modifier: Modifier = Modifier,
    instrument: InstrumentListItemModel
) {
    Column(modifier = modifier) {
        instrument.run {
            InformationListSeparatorHeader(R.string.instrument)
            type?.ifNotNullOrEmpty {
                TextWithHeading(headingRes = R.string.type, text = it)
            }

            ListSeparatorHeader(stringResource(id = R.string.description))
            description?.ifNotNullOrEmpty {
                SelectionContainer {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        text = it,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }
            }
        }
    }
}

// region Previews
@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            InstrumentDetailsScreen(
                instrument = InstrumentListItemModel(
                    id = "i1",
                    name = "baroque guitar",
                    disambiguation = "Baroque gut string guitar",
                    type = "String instrument",
                    description = "Predecessor of the modern classical guitar, it had gut strings and even gut frets. " +
                        "First described in 1555, it surpassed the Renaissance lute's popularity."
                )
            )
        }
    }
}
// endregion
