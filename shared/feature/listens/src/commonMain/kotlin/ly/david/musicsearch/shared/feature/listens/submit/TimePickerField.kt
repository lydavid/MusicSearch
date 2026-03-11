package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.SECONDS_IN_DAY
import ly.david.musicsearch.shared.domain.SECONDS_IN_HOUR
import ly.david.musicsearch.shared.domain.SECONDS_IN_MINUTE
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.common.toEpochSeconds
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Schedule
import ly.david.musicsearch.ui.common.modifier.onKeyboardClick
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cancel
import musicsearch.ui.common.generated.resources.ok
import musicsearch.ui.common.generated.resources.selectTime
import musicsearch.ui.common.generated.resources.time
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimePickerField(
    dateTimeEpochSeconds: Long,
    timeZone: TimeZone,
    modifier: Modifier = Modifier,
    onSelectTime: (timeSeconds: Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    val formattedTime = Instant.fromEpochSeconds(dateTimeEpochSeconds).getTimeFormatted(timeZone = timeZone)
    OutlinedTextField(
        value = formattedTime,
        onValueChange = { },
        label = { Text(stringResource(Res.string.time)) },
        trailingIcon = {
            Icon(imageVector = CustomIcons.Schedule, contentDescription = stringResource(Res.string.selectTime))
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
        TimePickerDialog(
            dateTimeEpochSeconds = dateTimeEpochSeconds,
            modifier = modifier,
            onDismiss = { showDialog = false },
            onSelectTime = onSelectTime,
            timeZone = TimeZone.currentSystemDefault(),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TimePickerDialog(
    dateTimeEpochSeconds: Long,
    timeZone: TimeZone,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelectTime: (timeSeconds: Long) -> Unit,
) {
    val epochSecondsUTC = Instant.fromEpochSeconds(dateTimeEpochSeconds)
        .toLocalDateTime(timeZone)
        .toInstant(TimeZone.UTC)
        .toEpochSeconds()
    val nonNegativeEpochSecondsUTC = (epochSecondsUTC + SECONDS_IN_DAY) % SECONDS_IN_DAY
    val initialHour = (nonNegativeEpochSecondsUTC / SECONDS_IN_HOUR).toInt()
    val initialMinute = (nonNegativeEpochSecondsUTC / SECONDS_IN_MINUTE % SECONDS_IN_MINUTE).toInt()
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
    )

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
            ) {
                TimePicker(state = timePickerState)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(stringResource(Res.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            val outputSeconds = timePickerState.hour * 60 * 60 + timePickerState.minute * 60
                            val epochSecondsLocal = Instant.fromEpochSeconds(outputSeconds.toLong())
                                .toLocalDateTime(TimeZone.UTC)
                                .toInstant(timeZone)
                                .toEpochMilliseconds() / MS_IN_SECOND
                            val nonOverflowEpochSecondsLocal = epochSecondsLocal % SECONDS_IN_DAY
                            onSelectTime(
                                nonOverflowEpochSecondsLocal,
                            )
                            onDismiss()
                        },
                    ) {
                        Text(stringResource(Res.string.ok))
                    }
                }
            }
        }
    }
}
