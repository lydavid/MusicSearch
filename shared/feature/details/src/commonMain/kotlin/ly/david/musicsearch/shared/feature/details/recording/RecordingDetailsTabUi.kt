package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.MusicVideo
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles
import kotlin.time.Instant

@Composable
internal fun RecordingDetailsTabUi(
    recording: RecordingDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    val strings = LocalStrings.current

    DetailsTabUi(
        detailsModel = recording,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        onCollapseExpandAliases = onCollapseExpandAliases,
        entityInfoSection = {
            if (video) {
                TextWithIcon(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp,
                    ),
                    imageVector = CustomIcons.MusicVideo,
                    text = strings.video,
                )
            }
            length?.ifNotNull {
                TextWithHeading(
                    heading = strings.length,
                    text = it.toDisplayTime(),
                    filterText = filterText,
                )
            }
            firstReleaseDate.ifNotEmpty {
                TextWithHeading(
                    heading = strings.firstReleaseDate,
                    text = it,
                    filterText = filterText,
                )
            }
            isrcs.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.isrc,
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }
        },
        bringYourOwnLabelsSection = {
            listenSection(
                recording = recording,
                now = detailsTabUiState.now,
            )
        },
    )
}

private fun LazyListScope.listenSection(
    recording: RecordingDetailsModel,
    now: Instant,
) {
    if (recording.listenCount != null) {
        item {
            ListSeparatorHeader(LocalStrings.current.listens)
        }
        item {
            ListItem(
                headlineContent = {
                    TextWithIcon(
                        imageVector = CustomIcons.Headphones,
                        text = recording.listenCount.toString(),
                    )
                },
            )
        }
        items(recording.latestListensTimestampsMs) {
            LastListenedListItem(
                lastListenedMs = it,
                now = now,
            )
        }
        // TODO: see all listens for a recording
        //  possibly go to the screen with all listens but with a filter applied, which the user can dismiss
//        item {
//            ClickableItem(
//                title = "See all listens",
//                endIcon = CustomIcons.ChevronRight,
//                onClick = {},
//            )
//        }
        item {
            UrlListItem(
                relation = RelationListItemModel(
                    id = "listenbrainz_url",
                    type = "ListenBrainz",
                    linkedEntity = MusicBrainzEntityType.URL,
                    name = recording.listenBrainzUrl,
                    linkedEntityId = "listenbrainz_url",
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LastListenedListItem(
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
