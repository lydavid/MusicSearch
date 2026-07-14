package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.common.transformOrEmptyIfNull
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.dialog.DialogWithCloseButton
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icons.AddLink
import ly.david.musicsearch.ui.common.icons.Album
import ly.david.musicsearch.ui.common.icons.CalendarMonth
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CollapseAll
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.DeleteOutline
import ly.david.musicsearch.ui.common.icons.ExpandAll
import ly.david.musicsearch.ui.common.icons.FilterAlt
import ly.david.musicsearch.ui.common.icons.FilterAltOff
import ly.david.musicsearch.ui.common.icons.Mic
import ly.david.musicsearch.ui.common.icons.OpenInNew
import ly.david.musicsearch.ui.common.icons.Person
import ly.david.musicsearch.ui.common.icons.Refresh
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.TextInput
import ly.david.musicsearch.ui.common.text.getFontWeightWithPreference
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.theme.getSubTextColor
import musicsearch.ui.common.generated.resources.MusicBrainzUrlMBID
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addMapping
import musicsearch.ui.common.generated.resources.delete
import musicsearch.ui.common.generated.resources.filterFromListenDate
import musicsearch.ui.common.generated.resources.goToAlbum
import musicsearch.ui.common.generated.resources.linkListenWithMusicBrainz
import musicsearch.ui.common.generated.resources.linkWithMusicBrainz
import musicsearch.ui.common.generated.resources.openInBrowser
import musicsearch.ui.common.generated.resources.refreshMapping
import musicsearch.ui.common.generated.resources.stopFilteringFromThisDate
import org.jetbrains.compose.resources.stringResource

// TODO:
@Composable
internal fun ListenAdditionalActionsBottomSheetContent(
    listen: ListenListItemModel,
    filterText: String,
    boldUnvisited: Boolean,
    showUnmappedData: Boolean,
    onToggleShowUnmappedData: () -> Unit = {},
    onGoToRelease: (releaseId: String) -> Unit = {},
    selectedEntityFacet: MusicBrainzEntity?,
    onFilterByEntityClick: (entity: MusicBrainzEntity) -> Unit = {},
    showMoreFilters: Boolean = false,
    onToggleMoreFilters: () -> Unit = {},
    filteringByThisDate: Boolean,
    onFilterByDate: (dateMilliseconds: Long) -> Unit = {},
    allowedToEdit: Boolean,
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
        PreviewSection(
            modifier = Modifier.padding(start = 16.dp),
            listen = listen,
            filterText = filterText,
            showUnmappedData = showUnmappedData,
            boldUnvisited = boldUnvisited,
            onToggleShowUnmappedData = onToggleShowUnmappedData,
        )

        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

        val releaseId = release.id.orEmpty()
        if (releaseId.isNotEmpty()) {
            ClickableItem(
                title = stringResource(Res.string.goToAlbum),
                startIcon = CustomIcons.Album,
                endIcon = CustomIcons.ChevronRight,
                fontWeight = release.getFontWeightWithPreference(boldUnvisited = boldUnvisited),
                onClick = {
                    onGoToRelease(releaseId)
                    onDismiss()
                },
            )

            HorizontalDivider(modifier = Modifier.padding())
        }

        FiltersSection(
            showMoreFilters = showMoreFilters,
            onToggleMoreFilters = onToggleMoreFilters,
            listen = listen,
            selectedEntityFacet = selectedEntityFacet,
            onFilterByEntityClick = onFilterByEntityClick,
            onDismiss = onDismiss,
            releaseId = releaseId,
            release = release,
            filteringByThisDate = filteringByThisDate,
            onFilterByDate = onFilterByDate,
        )

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
private fun FiltersSection(
    showMoreFilters: Boolean,
    onToggleMoreFilters: () -> Unit,
    listen: ListenListItemModel,
    selectedEntityFacet: MusicBrainzEntity?,
    onFilterByEntityClick: (MusicBrainzEntity) -> Unit,
    onDismiss: () -> Unit,
    releaseId: String,
    release: ListenRelease,
    filteringByThisDate: Boolean,
    onFilterByDate: (Long) -> Unit,
) {
    ClickableItem(
        title = "Filter by ...",
        startIcon = CustomIcons.FilterAlt,
        endIcon = if (showMoreFilters) CustomIcons.CollapseAll else CustomIcons.ExpandAll,
        onClick = onToggleMoreFilters,
    )

    if (showMoreFilters) {
        MoreFiltersSection(
            listen = listen,
            selectedEntityFacet = selectedEntityFacet,
            onFilterByEntityClick = onFilterByEntityClick,
            onDismiss = onDismiss,
            releaseId = releaseId,
            release = release,
        )
    }

    ClickableItem(
        title = stringResource(
            if (filteringByThisDate) {
                Res.string.stopFilteringFromThisDate
            } else {
                Res.string.filterFromListenDate
            },
        ),
        startIcon = CustomIcons.CalendarMonth,
        endIcon = if (filteringByThisDate) {
            CustomIcons.FilterAltOff
        } else {
            CustomIcons.FilterAlt
        },
        onClick = {
            onFilterByDate(listen.listenedAtMs)
            onDismiss()
        },
    )

    HorizontalDivider()
}

@Composable
private fun MoreFiltersSection(
    listen: ListenListItemModel,
    selectedEntityFacet: MusicBrainzEntity?,
    onFilterByEntityClick: (MusicBrainzEntity) -> Unit,
    onDismiss: () -> Unit,
    releaseId: String,
    release: ListenRelease,
) {
    RecordingFilter(
        listen = listen,
        selectedEntityFacet = selectedEntityFacet,
        onFilterByEntityClick = onFilterByEntityClick,
        onDismiss = onDismiss,
    )

    ReleaseFilter(
        releaseId = releaseId,
        selectedEntityFacet = selectedEntityFacet,
        release = release,
        onFilterByEntityClick = onFilterByEntityClick,
        onDismiss = onDismiss,
    )

    // TODO: filter by recording's work if it exists. there can be more than one (e.g. tracks with two songs)

    ArtistFilters(
        listen = listen,
        selectedEntityFacet = selectedEntityFacet,
        onFilterByEntityClick = onFilterByEntityClick,
        onDismiss = onDismiss,
    )
}

@Composable
private fun RecordingFilter(
    listen: ListenListItemModel,
    selectedEntityFacet: MusicBrainzEntity?,
    onFilterByEntityClick: (MusicBrainzEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    val recordingId = listen.recordingId
    val hasRecordingId = recordingId.isNotEmpty()
    val filteringByThisRecording = selectedEntityFacet?.type == MusicBrainzEntityType.RECORDING &&
        selectedEntityFacet.id == recordingId
    ClickableItem(
        title = when {
            filteringByThisRecording && hasRecordingId -> {
                "Stop filtering by this song"
            }

            hasRecordingId -> {
                "Filter by this song"
            }

            filteringByThisRecording -> {
                "Stop filtering by unlinked songs"
            }

            else -> {
                "Filter by unlinked songs"
            }
        },
        startIcon = CustomIcons.Mic,
        endIcon = if (filteringByThisRecording) {
            CustomIcons.FilterAltOff
        } else {
            CustomIcons.FilterAlt
        },
        onClick = {
            onFilterByEntityClick(
                MusicBrainzEntity(
                    id = recordingId,
                    type = MusicBrainzEntityType.RECORDING,
                ),
            )
            onDismiss()
        },
    )
}

@Composable
private fun ReleaseFilter(
    releaseId: String,
    selectedEntityFacet: MusicBrainzEntity?,
    release: ListenRelease,
    onFilterByEntityClick: (MusicBrainzEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    val hasReleaseId = releaseId.isNotEmpty()
    val filteringByThisRelease = selectedEntityFacet?.type == MusicBrainzEntityType.RELEASE &&
        selectedEntityFacet.id == releaseId

    val releaseName = release.name ?: "this album"
    ClickableItem(
        title = when {
            filteringByThisRelease && hasReleaseId -> {
                "Stop filtering by $releaseName"
            }

            hasReleaseId -> {
                "Filter by $releaseName"
            }

            filteringByThisRelease -> {
                "Stop filtering by unlinked albums"
            }

            else -> {
                "Filter by unlinked albums"
            }
        },
        startIcon = CustomIcons.Album,
        endIcon = if (filteringByThisRelease) {
            CustomIcons.FilterAltOff
        } else {
            CustomIcons.FilterAlt
        },
        onClick = {
            onFilterByEntityClick(
                MusicBrainzEntity(
                    id = releaseId,
                    type = MusicBrainzEntityType.RELEASE,
                ),
            )
            onDismiss()
        },
    )
}

@Composable
private fun ArtistFilters(
    listen: ListenListItemModel,
    selectedEntityFacet: MusicBrainzEntity?,
    onFilterByEntityClick: (MusicBrainzEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    listen.separateArtistCredits.forEach { (artistId, name, _) ->
        val filteringByThisArtist = artistId == selectedEntityFacet?.id
        ClickableItem(
            title = if (filteringByThisArtist) {
                "Stop filtering by $name"
            } else {
                "Filter by $name"
            },
            startIcon = CustomIcons.Person,
            endIcon = if (filteringByThisArtist) {
                CustomIcons.FilterAltOff
            } else {
                CustomIcons.FilterAlt
            },
            onClick = {
                onFilterByEntityClick(
                    MusicBrainzEntity(
                        id = artistId,
                        type = MusicBrainzEntityType.ARTIST,
                    ),
                )
                onDismiss()
            },
        )
    }
}

@Composable
private fun PreviewSection(
    modifier: Modifier,
    listen: ListenListItemModel,
    filterText: String,
    boldUnvisited: Boolean,
    showUnmappedData: Boolean,
    onToggleShowUnmappedData: () -> Unit = {},
) {
    val release = listen.release
    Row(
        modifier = modifier,
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
                    modifier = Modifier.padding(end = 4.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = TextStyles.getCardBodyTextStyle().fontSize,
                            ),
                        ) {
                            append(listen.getAnnotatedName(boldUnvisited = boldUnvisited))
                        }
                        withStyle(style = SpanStyle(fontSize = TextStyles.getCardBodySubTextStyle().fontSize)) {
                            append(" ${listen.durationMs.toDisplayTime()}")
                        }
                    },
                    highlightedText = filterText,
                    style = TextStyles.getCardBodyTextStyle(),
                )
                if (showUnmappedData) {
                    HighlightableText(
                        modifier = Modifier.padding(end = 4.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = TextStyles.getCardBodyTextStyle().fontSize,
                                    fontStyle = FontStyle.Italic,
                                    color = getSubTextColor(),
                                ),
                            ) {
                                append(listen.unmappedTrackName)

                                val discNumberString = listen.unmappedDiscNumber.transformOrEmptyIfNull { "$it." }
                                val trackNumberString = listen.unmappedTrackNumber.orEmpty()
                                val discTrack = "$discNumberString$trackNumberString"
                                append(discTrack.transformThisIfNotNullOrEmpty { " ($it)" })
                            }
                            // Could possibly show the recording duration here, but that may be confusing
                            // as the submitted data is the unmapped data.
                            // We're already showing the submitted duration above.
                        },
                        highlightedText = filterText,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                    ) {
                        HighlightableText(
                            text = listen.artistCredits,
                            highlightedText = filterText,
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                        if (showUnmappedData) {
                            HighlightableText(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontStyle = FontStyle.Italic,
                                            color = getSubTextColor(),
                                        ),
                                    ) {
                                        append(listen.unmappedFormattedArtistCredits)
                                    }
                                },
                                highlightedText = filterText,
                                style = TextStyles.getCardBodySubTextStyle(),
                            )
                        }

                        val releaseName = release.name
                        releaseName?.let { releaseName ->
                            HighlightableText(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = release.getFontWeightWithPreference(
                                                boldUnvisited = boldUnvisited,
                                            ),
                                        ),
                                    ) {
                                        append(releaseName)
                                    }
                                },
                                highlightedText = filterText,
                                modifier = Modifier.padding(top = 4.dp),
                                style = TextStyles.getCardBodySubTextStyle(),
                            )
                            if (showUnmappedData) {
                                release.unmappedName?.let { submittedReleaseName ->
                                    HighlightableText(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontStyle = FontStyle.Italic,
                                                    color = getSubTextColor(),
                                                ),
                                            ) {
                                                append(submittedReleaseName)
                                            }
                                        },
                                        highlightedText = filterText,
                                        style = TextStyles.getCardBodySubTextStyle(),
                                    )
                                }
                            }
                        }

                        if (showUnmappedData) {
                            listen.unmappedIsrc?.let { unmappedIsrc ->
                                Spacer(modifier = Modifier.padding(top = 4.dp))
                                listen.isrcs.joinToString(", ").ifNotEmpty { isrcs ->
                                    Text(
                                        text = isrcs,
                                        style = TextStyles.getCardBodySubTextStyle(),
                                    )
                                }
                                HighlightableText(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontStyle = FontStyle.Italic,
                                                color = getSubTextColor(),
                                            ),
                                        ) {
                                            append(unmappedIsrc)
                                        }
                                    },
                                    highlightedText = filterText,
                                    style = TextStyles.getCardBodySubTextStyle(),
                                )
                            }
                        }
                    }

                    IconButton(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .align(Alignment.CenterEnd),
                        onClick = onToggleShowUnmappedData,
                    ) {
                        Icon(
                            imageVector = if (showUnmappedData) CustomIcons.CollapseAll else CustomIcons.ExpandAll,
                            contentDescription = if (showUnmappedData) {
                                "Hide originally submitted data"
                            } else {
                                "Show originally submitted data"
                            },
                        )
                    }
                }
            }
        }
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
