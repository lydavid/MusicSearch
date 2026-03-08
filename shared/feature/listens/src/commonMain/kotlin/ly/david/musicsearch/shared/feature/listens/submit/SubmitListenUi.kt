package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.common.getShortDateFormatted
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cancel
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SubmitListenUi(
    state: SubmitListenUiState,
    timeZone: TimeZone,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val timestampIsStartTime = state.timestampIsStartTime
    val isCustomTime = state.isCustomTime
//    val dateEpochSeconds = state.dateEpochSeconds
//    val timeEpochSeconds = state.timeEpochSeconds
    val dateTimeEpochSeconds = state.dateTimeEpochSeconds

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

                val instant = Instant.fromEpochSeconds(dateTimeEpochSeconds)

                val time = instant.getTimeFormatted(timeZone = timeZone)

                // TODO: past 7pm, combining date + time will push the date to the next day
                //  before, it will not
                //  splitting date and time will also mess up during daylight saving
                val shortDate = instant.getShortDateFormatted(timeZone = timeZone)

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
                dateAndTimeEpochSeconds = dateTimeEpochSeconds,
                timeZone = timeZone,
                modifier = Modifier.padding(top = 8.dp),
                onSelectDate = {
                    eventSink(SubmitListenUiEvent.UpdateDate(it))
                },
            )

            TimePickerField(
                dateTimeEpochSeconds = dateTimeEpochSeconds,
                timeZone = timeZone,
                onSelectTime = {
                    eventSink(SubmitListenUiEvent.UpdateTime(it))
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
