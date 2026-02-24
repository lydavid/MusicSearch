package ly.david.musicsearch.shared.feature.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import ly.david.musicsearch.ui.common.theme.TextStyles
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LastListenedListItem(
    lastListenedMs: Long,
    now: Instant,
) {
    val instant = Instant.fromEpochMilliseconds(lastListenedMs)
    val formattedDateTimePeriod = formatPeriod(instant.getDateTimePeriod(now = now))
    val formattedDateTime = instant.getDateTimeFormatted()
    Text(
        text = "$formattedDateTimePeriod ($formattedDateTime)",
        modifier = Modifier.padding(horizontal = 16.dp),
        style = TextStyles.getCardBodyTextStyle(),
    )
}
