package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.dialog.DialogWithCloseButton
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icons.Album
import ly.david.musicsearch.ui.common.icons.ArrowBack
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Mic
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.text.TextInput
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilterTestTag

private const val ROTATE_DOWN = 90f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListensUi(
    state: ListensUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val strings = LocalStrings.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showDialog by rememberSaveable { mutableStateOf(false) }
    if (showDialog) {
        DialogWithCloseButton(
            onDismiss = { showDialog = false },
        ) {
            UsernameInput(
                listenBrainzUrl = state.listenBrainzUrl,
                text = state.textFieldText,
                strings = strings,
                onTextChange = {
                    eventSink(ListensUiEvent.EditText(it))
                },
                onSetUsername = {
                    eventSink(ListensUiEvent.SetUsername)
                    showDialog = false
                },
            )
        }
    }

    var showRecordingFacetBottomSheet: Boolean by remember { mutableStateOf(false) }
    if (showRecordingFacetBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRecordingFacetBottomSheet = false },
        ) {
            RecordingFacetBottomSheetContent(
                recordingFacetUiState = state.recordingFacetUiState,
                onQueryChange = {
                    eventSink(ListensUiEvent.UpdateFacetQuery(query = it))
                },
                onRecordingClick = {
                    eventSink(ListensUiEvent.ToggleRecordingFacet(it))
                },
                onDismiss = { showRecordingFacetBottomSheet = false },
            )
        }
    }

    val noUsernameSet = state.username.isEmpty()
    val lazyPagingItems = state.listensPagingDataFlow.collectAsLazyPagingItems()
    val overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = if (noUsernameSet) {
        null
    } else {
        {
            RefreshMenuItem(
                onClick = {
                    lazyPagingItems.refresh()
                },
            )
            OpenInBrowserMenuItem(
                url = state.userListensUrl,
            )
            DropdownMenuItem(
                text = {
                    Text(strings.changeUsername)
                },
                onClick = {
                    showDialog = true
                    closeMenu()
                },
            )
        }
    }
    val title = if (noUsernameSet) {
        strings.listens
    } else {
        strings.xListens(state.username)
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = {
                    eventSink(ListensUiEvent.NavigateUp)
                },
                title = title,
                subtitle = state.totalCountOfListens?.let { "$it songs" }.orEmpty(),
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = overflowDropdownMenuItems,
                additionalBar = {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        InputChip(
                            selected = state.recordingFacetUiState.selectedRecordingFacetId.isNotEmpty(),
                            leadingIcon = {
                                Icon(
                                    imageVector = CustomIcons.Mic,
                                    contentDescription = null,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = CustomIcons.ChevronRight,
                                    modifier = Modifier.rotate(ROTATE_DOWN),
                                    contentDescription = null,
                                )
                            },
                            label = {
                                Text(
                                    text = strings.recording,
                                )
                            },
                            onClick = {
                                showRecordingFacetBottomSheet = true
                            },
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismissBox(
                    state = rememberSwipeToDismissBoxState(),
                    backgroundContent = {},
                    content = { Snackbar(snackbarData) },
                )
            }
        },
    ) { innerPadding ->
        if (noUsernameSet) {
            UsernameInput(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                listenBrainzUrl = state.listenBrainzUrl,
                text = state.textFieldText,
                strings = strings,
                onTextChange = {
                    eventSink(ListensUiEvent.EditText(it))
                },
                onSetUsername = {
                    eventSink(ListensUiEvent.SetUsername)
                },
            )
        } else {
            var showBottomSheetForListen: ListenListItemModel? by remember { mutableStateOf(null) }
            showBottomSheetForListen?.let { listen ->
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheetForListen = null },
                ) {
                    ListenAdditionalActionsBottomSheetContent(
                        listen = listen,
                        onReleaseClick = { releaseId ->
                            eventSink(
                                ListensUiEvent.ClickItem(
                                    entity = MusicBrainzEntityType.RELEASE,
                                    id = releaseId,
                                ),
                            )
                        },
                        onDismiss = { showBottomSheetForListen = null },
                    )
                }
            }

            ScreenWithPagingLoadingAndError(
                lazyPagingItems = lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding),
                lazyListState = state.lazyListState,
                keyed = true,
            ) { listItemModel: Identifiable? ->
                when (listItemModel) {
                    is ListenListItemModel -> ListenListItem(
                        listen = listItemModel,
                        onClick = { id ->
                            eventSink(
                                ListensUiEvent.ClickItem(
                                    entity = MusicBrainzEntityType.RECORDING,
                                    id = id,
                                ),
                            )
                        },
                        onClickMoreActions = {
                            showBottomSheetForListen = listItemModel
                        },
                    )

                    is ListSeparator -> ListSeparatorHeader(
                        text = listItemModel.text,
                    )
                }
            }
        }
    }
}

@Composable
private fun UsernameInput(
    listenBrainzUrl: String,
    text: String,
    strings: AppStrings,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit = {},
    onSetUsername: () -> Unit = {},
) {
    TextInput(
        modifier = modifier,
        instructions = buildAnnotatedString {
            append("Enter your ")
            withLink(
                LinkAnnotation.Url(
                    listenBrainzUrl,
                    styles = TextLinkStyles(
                        style = SpanStyle(color = MaterialTheme.colorScheme.primary),
                        hoveredStyle = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                        ),
                        pressedStyle = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                ),
            ) {
                append("ListenBrainz")
            }
            append(" username to load your listens into the app.")
        },
        textLabel = strings.username,
        textHint = strings.enterUsername,
        text = text,
        buttonText = strings.set,
        onTextChange = onTextChange,
        onButtonClick = onSetUsername,
    )
}

@Composable
internal fun RecordingFacetBottomSheetContent(
    recordingFacetUiState: RecordingFacetUiState,
    onQueryChange: (String) -> Unit = {},
    onRecordingClick: (recordingId: String) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    // These are not collected until this UI is shown.
    val recordingFacets = recordingFacetUiState.recordingFacetsPagingDataFlow.collectAsLazyPagingItems()
    val strings = LocalStrings.current
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            val text = recordingFacetUiState.query
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier.testTag(TopAppBarWithFilterTestTag.FILTER_BACK.name),
                    ) {
                        Icon(
                            imageVector = CustomIcons.ArrowBack,
                            contentDescription = strings.cancel,
                        )
                    }
                },
                placeholder = { Text(strings.filter) },
                value = text,
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                trailingIcon = {
                    if (text.isEmpty()) return@TextField
                    IconButton(onClick = {
                        onQueryChange("")
                    }) {
                        Icon(
                            CustomIcons.Clear,
                            contentDescription = strings.clearFilter,
                        )
                    }
                },
                onValueChange = { newText ->
                    if (!newText.contains("\n")) {
                        onQueryChange(newText)
                    }
                },
            )
        }
        items(
            count = recordingFacets.itemCount,
            key = { index -> recordingFacets[index]?.id.orEmpty() },
        ) {
            recordingFacets[it]?.let { recordingFacet ->
                val selected = recordingFacetUiState.selectedRecordingFacetId == recordingFacet.id
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            } else {
                                Color.Unspecified
                            },
                        )
                        .clickable { onRecordingClick(recordingFacet.id) }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth()
                            .padding(end = 32.dp),
                    ) {
                        Text(
                            text = "${recordingFacet.getNameWithDisambiguation()} (${recordingFacet.count})",
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                        Text(
                            text = recordingFacet.formattedArtistCredits,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                    if (selected) {
                        Icon(
                            imageVector = CustomIcons.Check,
                            modifier = Modifier.align(Alignment.CenterEnd),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ListenAdditionalActionsBottomSheetContent(
    listen: ListenListItemModel,
    onReleaseClick: (releaseId: String) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val release = listen.release
    Column {
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
                    // TODO: allow faceting on this recording if exists
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
                    onReleaseClick(releaseId)
                    onDismiss()
                },
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}
