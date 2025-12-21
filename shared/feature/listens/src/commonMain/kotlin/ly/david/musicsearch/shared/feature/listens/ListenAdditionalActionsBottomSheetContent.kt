package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.dialog.DialogWithCloseButton
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icons.AddLink
import ly.david.musicsearch.ui.common.icons.Album
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.DeleteOutline
import ly.david.musicsearch.ui.common.icons.FilterAlt
import ly.david.musicsearch.ui.common.icons.FilterAltOff
import ly.david.musicsearch.ui.common.icons.Mic
import ly.david.musicsearch.ui.common.icons.Refresh
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.text.TextInput
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun ListenAdditionalActionsBottomSheetContent(
    listen: ListenListItemModel,
    filteringByThisRecording: Boolean,
    allowedToEdit: Boolean,
    onGoToReleaseClick: (releaseId: String) -> Unit = {},
    onFilterByRecordingClick: (recordingId: String) -> Unit = {},
    onSubmitMapping: (recordingMessyBrainzId: String, recordingId: String) -> Unit = { _, _ -> },
    onRefreshMapping: (recordingMessyBrainzId: String) -> Unit = {},
    onDelete: (listenedAtMs: Long, username: String, recordingMessyBrainzId: String) -> Unit = { _, _, _ -> },
    onDismiss: () -> Unit = {},
) {
    val release = listen.release
    Column(
        modifier = Modifier.verticalScroll(state = rememberScrollState()),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailImage(
                url = listen.imageUrl.orEmpty(),
                imageId = null,
                placeholderIcon = MusicBrainzEntityType.RECORDING.getIcon(),
            )

            SelectionContainer {
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    Text(
                        text = listen.name,
                        style = TextStyles.getCardBodyTextStyle(),
                        fontWeight = listen.fontWeight,
                    )
                    Text(
                        text = listen.formattedArtistCredits,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                    release.name?.let { releaseName ->
                        Text(
                            text = releaseName,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = release.fontWeight,
                        )
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

        release.id?.let { releaseId ->
            ClickableItem(
                title = "Go to album",
                startIcon = CustomIcons.Album,
                endIcon = CustomIcons.ChevronRight,
                fontWeight = release.fontWeight,
                onClick = {
                    onGoToReleaseClick(releaseId)
                    onDismiss()
                },
            )
        }

        val recordingId = listen.recordingId
        val hasRecordingId = recordingId.isNotEmpty()
        ClickableItem(
            title = when {
                filteringByThisRecording && hasRecordingId -> {
                    "Stop filtering by this song"
                }

                hasRecordingId -> {
                    "Filter listens by this song"
                }

                filteringByThisRecording -> {
                    "Stop filtering by unlinked songs"
                }

                else -> {
                    "Filter listens by unlinked songs"
                }
            },
            startIcon = CustomIcons.Mic,
            endIcon = if (filteringByThisRecording) {
                CustomIcons.FilterAltOff
            } else {
                CustomIcons.FilterAlt
            },
            onClick = {
                onFilterByRecordingClick(recordingId)
                onDismiss()
            },
        )

        if (allowedToEdit) {
            HorizontalDivider()

            LinkWithMusicBrainzItem(
                onSubmitMapping = {
                    onSubmitMapping(listen.recordingMessybrainzId, it)
                    onDismiss()
                },
            )

            ClickableItem(
                title = "Refresh mapping",
                startIcon = CustomIcons.Refresh,
                onClick = {
                    onRefreshMapping(listen.recordingMessybrainzId)
                    onDismiss()
                },
            )

            HorizontalDivider()

            ClickableItem(
                title = "Delete",
                startIcon = CustomIcons.DeleteOutline,
                foregroundColor = MaterialTheme.colorScheme.error,
                onClick = {
                    onDelete(
                        listen.listenedAtMs,
                        listen.username,
                        listen.recordingMessybrainzId,
                    )
                    onDismiss()
                },
            )
        }

        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}

@Composable
private fun LinkWithMusicBrainzItem(
    onSubmitMapping: (recordingMessyBrainzId: String) -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var mbid by rememberSaveable { mutableStateOf("") }
    if (showDialog) {
        DialogWithCloseButton(
            onDismiss = { showDialog = false },
        ) {
            TextInput(
                instructions = buildAnnotatedString {
                    append("Link listen with MusicBrainz to get metadata")
                },
                text = mbid,
                textLabel = "MusicBrainz URL/MBID",
                textHint = null,
                onTextChange = { mbid = it },
                buttonText = "Add mapping",
                onButtonClick = {
                    onSubmitMapping(mbid)
                },
                modifier = Modifier,
            )
        }
    }

    ClickableItem(
        title = "Link with MusicBrainz",
        startIcon = CustomIcons.AddLink,
        onClick = {
            showDialog = true
        },
    )
}
