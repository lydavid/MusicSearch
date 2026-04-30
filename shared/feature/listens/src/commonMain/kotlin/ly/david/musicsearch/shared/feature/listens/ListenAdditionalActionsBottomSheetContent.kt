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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
import ly.david.musicsearch.ui.common.icons.OpenInNew
import ly.david.musicsearch.ui.common.icons.Refresh
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.TextInput
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.MusicBrainzUrlMBID
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addMapping
import musicsearch.ui.common.generated.resources.delete
import musicsearch.ui.common.generated.resources.goToAlbum
import musicsearch.ui.common.generated.resources.linkListenWithMusicBrainz
import musicsearch.ui.common.generated.resources.linkWithMusicBrainz
import musicsearch.ui.common.generated.resources.openInBrowser
import musicsearch.ui.common.generated.resources.refreshMapping
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ListenAdditionalActionsBottomSheetContent(
    listen: ListenListItemModel,
    filterText: String,
    filteringByThisRecording: Boolean,
    allowedToEdit: Boolean,
    onGoToReleaseClick: (releaseId: String) -> Unit = {},
    onFilterByRecordingClick: (recordingId: String) -> Unit = {},
    onSubmitMapping: (recordingMessyBrainzId: String, recordingId: String) -> Unit = { _, _ -> },
    onRefreshMapping: (recordingMessyBrainzId: String) -> Unit = {},
    onOpenInBrowser: (listenedAtMs: Long) -> Unit = {},
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
                imageMetadata = listen.imageMetadata,
                placeholderIcon = MusicBrainzEntityType.RECORDING.getIcon(),
                enableSharedTransition = false,
            )

            SelectionContainer {
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    HighlightableText(
                        text = listen.getAnnotatedName(),
                        highlightedText = filterText,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                    HighlightableText(
                        text = listen.formattedArtistCredits,
                        highlightedText = filterText,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                    release.name?.let { releaseName ->
                        HighlightableText(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = release.fontWeight)) {
                                    append(releaseName)
                                }
                            },
                            highlightedText = filterText,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

        release.id?.let { releaseId ->
            ClickableItem(
                title = stringResource(Res.string.goToAlbum),
                startIcon = CustomIcons.Album,
                endIcon = CustomIcons.ChevronRight,
                fontWeight = release.fontWeight,
                onClick = {
                    onGoToReleaseClick(releaseId)
                    onDismiss()
                },
            )
        }

        // TODO: Filter by ..., then clicking gives option to filter by song/release/work/artists
        //  move the filter icon to start
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

        // TODO: filter by date

        HorizontalDivider()

        if (allowedToEdit) {
            LinkWithMusicBrainzItem(
                onSubmitMapping = {
                    onSubmitMapping(listen.recordingMessybrainzId, it)
                    onDismiss()
                },
            )

            ClickableItem(
                title = stringResource(Res.string.refreshMapping),
                startIcon = CustomIcons.Refresh,
                onClick = {
                    onRefreshMapping(listen.recordingMessybrainzId)
                    onDismiss()
                },
            )
        }

        ClickableItem(
            title = stringResource(Res.string.openInBrowser),
            startIcon = CustomIcons.OpenInNew,
            onClick = {
                onOpenInBrowser(listen.listenedAtMs)
                onDismiss()
            },
        )

        if (allowedToEdit) {
            HorizontalDivider()

            ClickableItem(
                title = stringResource(Res.string.delete),
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
                    append(stringResource(Res.string.linkListenWithMusicBrainz))
                },
                text = mbid,
                textLabel = stringResource(Res.string.MusicBrainzUrlMBID),
                textHint = null,
                onTextChange = { mbid = it },
                buttonText = stringResource(Res.string.addMapping),
                onButtonClick = {
                    onSubmitMapping(mbid)
                },
                modifier = Modifier,
            )
        }
    }

    ClickableItem(
        title = stringResource(Res.string.linkWithMusicBrainz),
        startIcon = CustomIcons.AddLink,
        onClick = {
            showDialog = true
        },
    )
}
