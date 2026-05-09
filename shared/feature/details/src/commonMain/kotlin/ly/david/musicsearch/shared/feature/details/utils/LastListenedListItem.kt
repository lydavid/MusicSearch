package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.common.DateTimeFormat
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LastListenedListItem(
    lastListenedMs: Long,
    now: Instant,
    filterText: String,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
) {
    val instant = Instant.fromEpochMilliseconds(lastListenedMs)
    val formattedDateTimePeriod = formatPeriod(instant.getDateTimePeriod(now = now))
    val formattedDateTime = instant.getDateTimeFormatted(format = DateTimeFormat.MediumDateTime)
    ClickableItem(
        title = "$formattedDateTimePeriod ($formattedDateTime)",
        filterText = filterText,
        endIcon = CustomIcons.ChevronRight,
        onClick = {
            onGoToListenAtEpochSeconds(lastListenedMs / MS_IN_SECOND)
        },
    )
}
