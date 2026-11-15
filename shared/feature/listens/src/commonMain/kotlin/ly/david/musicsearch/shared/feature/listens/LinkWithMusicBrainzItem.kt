package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.recording.RecordingsMatchingIsrcResult
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.dialog.DialogWithCloseButton
import ly.david.musicsearch.ui.common.icons.AddLink
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.recording.RecordingByIsrcListItem
import ly.david.musicsearch.ui.common.text.SyncingText
import ly.david.musicsearch.ui.common.text.TextInput
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.MusicBrainzUrlMBID
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addMapping
import musicsearch.ui.common.generated.resources.linkListenWithMusicBrainz
import musicsearch.ui.common.generated.resources.linkWithMusicBrainz
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LinkWithMusicBrainzItem(
    linkUiState: LinkUiState,
    boldUnvisited: Boolean,
    onOpenLinkDialog: () -> Unit,
    onCloseLinkDialog: () -> Unit,
    onGoToRecording: (recordingId: String) -> Unit,
    onSubmitMapping: (recordingMusicBrainzId: String) -> Unit,
    onSelectAutomatic: (isAutomatic: Boolean) -> Unit,
) {
    if (linkUiState.showLinkDialog) {
        LinkWithMusicBrainzDialog(
            linkUiState = linkUiState,
            boldUnvisited = boldUnvisited,
            onGoToRecording = onGoToRecording,
            onSubmitMapping = onSubmitMapping,
            onDismiss = onCloseLinkDialog,
            onSelectAutomatic = onSelectAutomatic,
        )
    }

    ClickableItem(
        title = stringResource(Res.string.linkWithMusicBrainz),
        startIcon = CustomIcons.AddLink,
        endIcon = CustomIcons.ChevronRight,
        onClick = onOpenLinkDialog,
    )
}

@Composable
private fun LinkWithMusicBrainzDialog(
    linkUiState: LinkUiState,
    boldUnvisited: Boolean,
    onSubmitMapping: (recordingMusicBrainzId: String) -> Unit,
    onGoToRecording: (recordingId: String) -> Unit,
    onDismiss: () -> Unit,
    onSelectAutomatic: (isAutomatic: Boolean) -> Unit,
) {
    DialogWithCloseButton(
        onDismiss = onDismiss,
    ) {
        LinkWithMusicBrainzDialogContent(
            linkUiState = linkUiState,
            boldUnvisited = boldUnvisited,
            onSubmitMapping = onSubmitMapping,
            onGoToRecording = onGoToRecording,
            onSelectAutomatic = onSelectAutomatic,
        )
    }
}

@Composable
internal fun LinkWithMusicBrainzDialogContent(
    linkUiState: LinkUiState = LinkUiState(),
    boldUnvisited: Boolean = true,
    onSubmitMapping: (recordingMusicBrainzId: String) -> Unit = {},
    onGoToRecording: (recordingId: String) -> Unit = {},
    onSelectAutomatic: (isAutomatic: Boolean) -> Unit = {},
) {
    var mbid by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(Res.string.linkListenWithMusicBrainz),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        val isAutomatic = linkUiState.isAutomatic
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 24.dp),
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2,
                ),
                onClick = { onSelectAutomatic(true) },
                selected = isAutomatic,
                label = { Text("Automatic") },
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2,
                ),
                onClick = { onSelectAutomatic(false) },
                selected = !isAutomatic,
                label = { Text("Manual") },
            )
        }

        if (isAutomatic) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 24.dp),
            ) {
                when (val result = linkUiState.recordingsMatchingIsrcResult) {
                    RecordingsMatchingIsrcResult.Loading -> {
                        SyncingText(message = "Searching...")
                    }

                    RecordingsMatchingIsrcResult.NoUnmappedIsrc -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Text(
                                "This listen has no ISRC, so we can’t automatically match it.",
                                style = TextStyles.getCardBodySubTextStyle(),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }

                    RecordingsMatchingIsrcResult.NoMatchingRecordings -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Text(
                                "No recordings matched this listen's ISRC.",
                                style = TextStyles.getCardBodySubTextStyle(),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }

                    is RecordingsMatchingIsrcResult.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            contentPadding = PaddingValues(0.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            items(items = result.recordings, key = { it.id }) { recording ->
                                RecordingByIsrcListItem(
                                    recording = recording,
                                    boldUnvisited = boldUnvisited,
                                    onRecordingClick = {
                                        onGoToRecording(recording.id)
                                    },
                                    onSelect = {
                                        // Consider making select highlight the item, and enable a submit button.
                                        // It's an extra click, but submission from just an icon button is ambiguous.
                                        onSubmitMapping(recording.id)
                                    },
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        } else {
            TextInput(
                instructions = AnnotatedString(text = ""),
                text = mbid,
                textLabel = stringResource(Res.string.MusicBrainzUrlMBID),
                textHint = null,
                onTextChange = {
                    mbid = it
                },
                buttonText = stringResource(Res.string.addMapping),
                onButtonClick = { onSubmitMapping(mbid) },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
