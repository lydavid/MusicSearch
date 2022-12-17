package ly.david.mbjc.ui.instrument.details

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.InstrumentListItemModel
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading

@Composable
internal fun InstrumentDetailsScreen(
    modifier: Modifier = Modifier,
    instrument: InstrumentListItemModel
) {
    Column(modifier = modifier) {
        instrument.run {
            // TODO: header
            type?.ifNotNullOrEmpty {
                TextWithHeading(headingRes = R.string.type, text = it)
            }
            // TODO: header
            description?.ifNotNullOrEmpty {
                TextWithHeading(headingRes = R.string.description, text = it)
            }
        }
    }
}
