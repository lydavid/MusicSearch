package ly.david.musicsearch.shared.feature.listens.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.common.toEpochSeconds
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cancel
import musicsearch.ui.common.generated.resources.ok
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

// https://github.com/metabrainz/listenbrainz-server/blob/master/listenbrainz/listenstore/__init__.py#L15
private const val MINIMUM_TIMESTAMP = 1033410600L * MS_IN_SECOND
private const val MINIMUM_YEAR = 2002

/**
 * Only meant for use in the context of ListenBrainz integration because it has some assumptions about date ranges.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DatePickerDialog(
    dateTimeEpochSeconds: Long?,
    timeZone: TimeZone,
    clock: Clock,
    onSelectDate: (dateSeconds: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    // Shift timezone because of this bug: https://issuetracker.google.com/issues/281859606
    val currentSelectedDateMillisUTC: Long? = dateTimeEpochSeconds?.let {
        Instant.fromEpochSeconds(dateTimeEpochSeconds)
            .toLocalDateTime(timeZone)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()
    }

    val currentDateUTC = clock.now()
        .toLocalDateTime(timeZone)
        .toInstant(TimeZone.UTC)
    val currentDateMillisUTC = currentDateUTC.toEpochMilliseconds()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentSelectedDateMillisUTC,
        yearRange = IntRange(MINIMUM_YEAR, currentDateUTC.toLocalDateTime(TimeZone.UTC).year),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val afterMin = utcTimeMillis > MINIMUM_TIMESTAMP
                val beforeMax = utcTimeMillis <= currentDateMillisUTC
                return afterMin && beforeMax
            }
        },
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selectedDateMillis = datePickerState.selectedDateMillis ?: 0

                // This will be the date selected in the calendar in the system timezone at midnight.
                val selectedDateSecondsWithOffset = Instant.fromEpochMilliseconds(selectedDateMillis)
                    .toLocalDateTime(TimeZone.UTC)
                    .toInstant(timeZone)
                    .toEpochSeconds()

                // We are returning this selected time as if it were UTC.
                onSelectDate(
                    selectedDateSecondsWithOffset,
                )
                onDismiss()
            }) {
                Text(stringResource(Res.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
