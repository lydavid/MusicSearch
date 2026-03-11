package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.common.getShortDateFormatted
import ly.david.musicsearch.shared.domain.common.toEpochSeconds
import ly.david.musicsearch.ui.common.icons.CalendarMonth
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.modifier.onKeyboardClick
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cancel
import musicsearch.ui.common.generated.resources.date
import musicsearch.ui.common.generated.resources.ok
import musicsearch.ui.common.generated.resources.selectDate
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Modified from https://developer.android.com/develop/ui/compose/components/datepickers.
 */
@Composable
internal fun DatePickerField(
    dateTimeEpochSeconds: Long,
    timeZone: TimeZone,
    clock: Clock,
    modifier: Modifier = Modifier,
    onSelectDate: (dateSeconds: Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate = Instant.fromEpochSeconds(dateTimeEpochSeconds).getShortDateFormatted(timeZone = timeZone)
    OutlinedTextField(
        value = formattedDate,
        onValueChange = { },
        label = { Text(stringResource(Res.string.date)) },
        trailingIcon = {
            Icon(imageVector = CustomIcons.CalendarMonth, contentDescription = stringResource(Res.string.selectDate))
        },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(dateTimeEpochSeconds) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showDialog = true
                    }
                }
            }
            .onKeyboardClick {
                showDialog = true
            },
    )

    if (showDialog) {
        DatePickerDialog(
            dateTimeEpochSeconds = dateTimeEpochSeconds,
            timeZone = timeZone,
            clock = clock,
            onSelectDate = onSelectDate,
            onDismiss = { showDialog = false },
        )
    }
}

// https://github.com/metabrainz/listenbrainz-server/blob/master/listenbrainz/listenstore/__init__.py#L15
private const val MINIMUM_TIMESTAMP = 1033410600L * MS_IN_SECOND
private const val MINIMUM_YEAR = 2002

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    dateTimeEpochSeconds: Long,
    timeZone: TimeZone,
    clock: Clock,
    onSelectDate: (dateSeconds: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    // Shift timezone because of this bug: https://issuetracker.google.com/issues/281859606
    val currentSelectedDateMillisUTC = Instant.fromEpochSeconds(dateTimeEpochSeconds)
        .toLocalDateTime(timeZone)
        .toInstant(TimeZone.UTC)
        .toEpochMilliseconds()

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
