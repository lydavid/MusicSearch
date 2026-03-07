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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.SECONDS_IN_DAY
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.common.getShortDateFormatted
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.icons.CalendarMonth
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Schedule
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cancel
import musicsearch.ui.common.generated.resources.ok
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SubmitListenUi(
    state: SubmitListenUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val timestampIsStartTime = state.timestampIsStartTime
    val isCustomTime = state.isCustomTime
    val dateTimestampSeconds = state.dateTimestampSeconds
    val timeTimestampSeconds = state.timeTimestampSeconds

    LaunchedEffect(Unit) {
        eventSink(SubmitListenUiEvent.UpdateTimestampsToNow)
    }

    LaunchedEffect(isCustomTime) {
        if (!isCustomTime) {
            while (true) {
                eventSink(SubmitListenUiEvent.UpdateTimestampsToNow)
                delay(MS_IN_SECOND.toLong())
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            text = "Submit listen to ListenBrainz",
            style = TextStyles.getHeaderTextStyle(),
        )

        when (val type = state.submitListenType) {
            is SubmitListenType.Track -> {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                            ),
                        ) {
                            append(type.getAnnotatedName())
                        }
                        withStyle(style = SpanStyle(fontSize = 15.sp)) {
                            append(" ${type.lengthMilliseconds.toDisplayTime()}")
                        }
                    },
                    style = TextStyles.getCardBodyTextStyle(),
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = type.artists.getDisplayNames(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                )

//                val adjustedSeconds = state.dateTimestampSeconds + state.timeTimestampSeconds -
//                    if (state.timestampIsStartTime) {
//                        0
//                    } else {
//                        (state.listenType.lengthMilliseconds ?: 0) / MS_IN_SECOND
//                    }


                val timeInstant = Instant.fromEpochSeconds(timeTimestampSeconds)
                val time = timeInstant.getTimeFormatted(inUtc = false)

                // TODO: past 7pm, combining date + time will push the date to the next day
                //  before, it will not
                val dateInstant = Instant.fromEpochSeconds(dateTimestampSeconds)
                val shortDate = dateInstant.getShortDateFormatted(inUtc = false)


                println("findme: timeTimestampSeconds=$timeTimestampSeconds")
                println("findme: dateTimestampSeconds=$dateTimestampSeconds")

                Text(
                    text = "$shortDate$DOT_SEPARATOR$time",
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(top = 8.dp),
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2,
                ),
                onClick = { eventSink(SubmitListenUiEvent.UpdateTimestampIsStartTime(true)) },
                selected = timestampIsStartTime,
                label = { Text("Started") },
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2,
                ),
                onClick = { eventSink(SubmitListenUiEvent.UpdateTimestampIsStartTime(false)) },
                selected = !timestampIsStartTime,
                label = { Text("Finished") },
            )
        }

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(top = 8.dp),
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2,
                ),
                onClick = { eventSink(SubmitListenUiEvent.UpdateIsCustomTime(false)) },
                selected = !isCustomTime,
                label = { Text("Now") },
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2,
                ),
                onClick = { eventSink(SubmitListenUiEvent.UpdateIsCustomTime(true)) },
                selected = isCustomTime,
                label = { Text("Custom") },
            )
        }

        if (isCustomTime) {
            DatePickerField(
                dateTimestampSeconds = dateTimestampSeconds,
                timeTimestampSeconds = timeTimestampSeconds,
                onSelectDate = {
                    eventSink(SubmitListenUiEvent.UpdateDateTimestamp(it))
                },
                modifier = Modifier.padding(top = 8.dp),
            )

            TimePickerField(
                timeTimestampSeconds = timeTimestampSeconds,
                onSelectTime = {
                    eventSink(SubmitListenUiEvent.UpdateTimeTimestamp(it))
                },
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = { eventSink(SubmitListenUiEvent.Dismiss) },
            ) {
                Text(stringResource(Res.string.cancel))
            }
            TextButton(
                onClick = { eventSink(SubmitListenUiEvent.Submit) },
            ) {
                Text("Submit")
            }
        }
    }
}

/**
 * Modified from https://developer.android.com/develop/ui/compose/components/datepickers.
 */
@Composable
internal fun DatePickerField(
    dateTimestampSeconds: Long,
    timeTimestampSeconds: Long,
    modifier: Modifier = Modifier,
    onSelectDate: (dateSeconds: Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate = Instant.fromEpochSeconds(dateTimestampSeconds).getShortDateFormatted(inUtc = false)
    OutlinedTextField(
        value = formattedDate,
        onValueChange = { },
        label = { Text("YYYY-MM-DD") },
        trailingIcon = {
            Icon(imageVector = CustomIcons.CalendarMonth, contentDescription = "Select date")
        },
        readOnly = false,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(dateTimestampSeconds) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showDialog = true
                    }
                }
            },
    )

    if (showDialog) {
        DatePickerDialog(
            dateTimeStampSeconds = dateTimestampSeconds,
            onSelectDate = onSelectDate,
            onDismiss = { showDialog = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    dateTimeStampSeconds: Long,
    onSelectDate: (dateSeconds: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    // This is in UTC.
    val dateTimeUTC = Instant.fromEpochSeconds(dateTimeStampSeconds)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toInstant(TimeZone.UTC)
        .toEpochMilliseconds()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateTimeUTC)//dateTimeStampSeconds * MS_IN_SECOND)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val datePickerResult = datePickerState.selectedDateMillis ?: 0

                // This will be the date selected in the calendar in the system timezone at midnight.
                val offsetToLocalTimeZone = Instant.fromEpochMilliseconds(datePickerResult)
                    .toLocalDateTime(TimeZone.UTC)
                    .toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
                val dateSeconds = offsetToLocalTimeZone / MS_IN_SECOND
                println("findme: dateSeconds=$dateSeconds")

                // We are returning this selected time as if it were UTC.
                // We only did the timezone shift because of this bug: https://issuetracker.google.com/issues/281859606
                onSelectDate(
                    dateSeconds,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimePickerField(
    timeTimestampSeconds: Long,
    modifier: Modifier = Modifier,
    onSelectTime: (timeSeconds: Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    val formattedTime = Instant.fromEpochSeconds(timeTimestampSeconds).getTimeFormatted(inUtc = false)
    OutlinedTextField(
        value = formattedTime,
        onValueChange = { },
        label = { Text("HH:MM") },
        trailingIcon = {
            Icon(imageVector = CustomIcons.Schedule, contentDescription = "Select time")
        },
        readOnly = false,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(timeTimestampSeconds) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showDialog = true
                    }
                }
            },
    )

    if (showDialog) {
        TimePickerDialog(
            timeTimestampSeconds = timeTimestampSeconds,
            modifier = modifier,
            onDismiss = { showDialog = false },
            onSelectTime = onSelectTime,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TimePickerDialog(
    timeTimestampSeconds: Long,
    modifier: Modifier,
    onDismiss: () -> Unit,
    onSelectTime: (timeSeconds: Long) -> Unit,
) {
    val epochSecondsUTC = Instant.fromEpochSeconds(timeTimestampSeconds)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toInstant(TimeZone.UTC)
        .toEpochMilliseconds() / MS_IN_SECOND
    val nonNegativeEpochSecondsUTC = (epochSecondsUTC + SECONDS_IN_DAY) % SECONDS_IN_DAY
    val initialHour = (nonNegativeEpochSecondsUTC / 60 / 60).toInt()
    val initialMinute = (nonNegativeEpochSecondsUTC / 60 % 60).toInt()
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
    )

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
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
                            val outputTimeSeconds = timePickerState.hour * 60 * 60 + timePickerState.minute * 60
                            val timeSecondsLocal = Instant.fromEpochSeconds(outputTimeSeconds.toLong())
                                .toLocalDateTime(TimeZone.UTC)
                                .toInstant(TimeZone.currentSystemDefault())
                                .toEpochMilliseconds() / MS_IN_SECOND
                            onSelectTime(
                                timeSecondsLocal % SECONDS_IN_DAY
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
