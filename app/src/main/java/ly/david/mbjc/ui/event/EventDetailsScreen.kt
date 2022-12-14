package ly.david.mbjc.ui.event

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.EventListItemModel
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading

@Composable
internal fun EventDetailsScreen(
    modifier: Modifier = Modifier,
    event: EventListItemModel,
) {
    Column(modifier = modifier) {
        event.run {
            type?.ifNotNullOrEmpty {
                TextWithHeading(headingRes = R.string.type, text = it)
            }
            lifeSpan?.run {
                // TODO: if same, just say "Date"
                begin?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.start_date, text = it)
                }
                end?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.end_date, text = it)
                }
            }
            // TODO: time

            // TODO: set list
            //  api seems strange?
        }
    }
}
