package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.TimeZone
import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.common.getShortDateFormatted
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cancel
import musicsearch.ui.common.generated.resources.custom
import musicsearch.ui.common.generated.resources.finished
import musicsearch.ui.common.generated.resources.now
import musicsearch.ui.common.generated.resources.started
import musicsearch.ui.common.generated.resources.startedAt
import musicsearch.ui.common.generated.resources.submit
import musicsearch.ui.common.generated.resources.submitAlbumListenToListenBrainz
import musicsearch.ui.common.generated.resources.submitTrackListenToListenBrainz
import musicsearch.ui.common.generated.resources.xTracksWithYLength
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SubmitListenUi(
    state: SubmitListenUiState,
    timeZone: TimeZone,
    clock: Clock,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val timestampIsStartTime = state.timestampIsStartTime
    val useCustomTime = state.useCustomTime
    val dateTimeEpochSeconds = state.dateTimeEpochSeconds
    val listenedAtDateTimeEpochSeconds = state.listenedAtDateTimeEpochSeconds

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        when (val type = state.submitListenType) {
            is SubmitListenType.Track -> {
                Text(
                    text = stringResource(Res.string.submitTrackListenToListenBrainz),
                    style = TextStyles.getHeaderTextStyle(),
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = TextStyles.getCardBodyTextStyle().fontSize,
                            ),
                        ) {
                            append(type.info.getAnnotatedName())
                        }
                        withStyle(style = SpanStyle(fontSize = TextStyles.getCardBodySubTextStyle().fontSize)) {
                            append(" ${type.info.lengthMilliseconds.toDisplayTime()}")
                        }
                    },
                    style = TextStyles.getCardBodyTextStyle(),
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 16.dp),
                )

                Text(
                    text = type.info.artists.getDisplayNames(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                )

                type.releaseName?.let { releaseName ->
                    Text(
                        text = releaseName,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }

            is SubmitListenType.Album -> {
                Text(
                    text = stringResource(Res.string.submitAlbumListenToListenBrainz),
                    style = TextStyles.getHeaderTextStyle(),
                )

                Text(
                    text = type.releaseName,
                    style = TextStyles.getCardBodyTextStyle(),
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 16.dp),
                )

                Text(
                    text = type.releaseArtists.getDisplayNames(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                )

                val numberOfTracks = type.recordingIds.size
                val totalFormattedLength = state.allSelectedTrackInfo?.getTotalListenLength().toDisplayTime()
                Text(
                    text = stringResource(
                        Res.string.xTracksWithYLength,
                        numberOfTracks,
                        totalFormattedLength,
                    ),
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }

        val instant = Instant.fromEpochSeconds(listenedAtDateTimeEpochSeconds)
        val time = instant.getTimeFormatted(timeZone = timeZone)
        val shortDate = instant.getShortDateFormatted(timeZone = timeZone)

        Text(
            text = stringResource(Res.string.startedAt, "$shortDate$DOT_SEPARATOR$time"),
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyles.getCardBodySubTextStyle(),
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(top = 8.dp),
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2,
                ),
                onClick = { eventSink(SubmitListenUiEvent.UpdateDateTimeIsStartTime(true)) },
                selected = timestampIsStartTime,
                label = { Text(stringResource(Res.string.started)) },
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2,
                ),
                onClick = { eventSink(SubmitListenUiEvent.UpdateDateTimeIsStartTime(false)) },
                selected = !timestampIsStartTime,
                label = { Text(stringResource(Res.string.finished)) },
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
                onClick = { eventSink(SubmitListenUiEvent.UpdateUseCustomTime(false)) },
                selected = !useCustomTime,
                label = { Text(stringResource(Res.string.now)) },
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2,
                ),
                onClick = { eventSink(SubmitListenUiEvent.UpdateUseCustomTime(true)) },
                selected = useCustomTime,
                label = { Text(stringResource(Res.string.custom)) },
            )
        }

        if (useCustomTime) {
            DatePickerField(
                dateTimeEpochSeconds = dateTimeEpochSeconds,
                timeZone = timeZone,
                clock = clock,
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
                Text(stringResource(Res.string.submit))
            }
        }
    }
}
