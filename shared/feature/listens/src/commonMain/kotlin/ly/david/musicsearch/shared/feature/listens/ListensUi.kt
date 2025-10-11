package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.dialog.DialogWithCloseButton
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.FilterAlt
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.text.TextInput
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter

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

    var showFacetsBottomSheet: Boolean by remember { mutableStateOf(false) }
    if (showFacetsBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showFacetsBottomSheet = false
            },
        ) {
            FacetsBottomSheetContent(
                state = state.facetsUiState,
                onUpdateTab = {
                    eventSink(ListensUiEvent.UpdateFacetTab(it))
                },
                onFacetClick = {
                    eventSink(ListensUiEvent.ToggleFacet(it))
                },
                onDismiss = {
                    showFacetsBottomSheet = false
                },
            )
        }
    }

    val noUsernameSet = state.noUsernameSet
    val title = if (state.noUsernameSet) {
        strings.listens
    } else {
        strings.xListens(state.username)
    }
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
    val selectedEntityFacet = state.facetsUiState.selectedEntityFacet
    val additionalBar: @Composable (() -> Unit) = if (noUsernameSet) {
        {}
    } else {
        {
            Row(
                modifier = Modifier.padding(end = 16.dp),
            ) {
                val selectedFacet = selectedEntityFacet != null
                if (selectedFacet) {
                    IconButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = {
                            eventSink(ListensUiEvent.ToggleFacet(selectedEntityFacet))
                        },
                    ) {
                        Icon(
                            imageVector = CustomIcons.Clear,
                            contentDescription = "Clear facets",
                        )
                    }
                }
                InputChip(
                    modifier = Modifier.padding(
                        start = if (selectedFacet) 4.dp else 16.dp,
                    ),
                    selected = selectedFacet,
                    leadingIcon = {
                        Icon(
                            imageVector = CustomIcons.FilterAlt,
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
                            text = "Facets",
                        )
                    },
                    onClick = {
                        showFacetsBottomSheet = true
                    },
                )
            }
        }
    }

    state.actionableResult?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(
                message = it.message,
            )
        }
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
                additionalBar = additionalBar,
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
                        filteringByThisRecording = listen.recordingId == selectedEntityFacet?.id,
                        allowedToEdit = state.browsingUserIsSameAsLoggedInUser,
                        onGoToReleaseClick = { releaseId ->
                            eventSink(
                                ListensUiEvent.ClickItem(
                                    entity = MusicBrainzEntityType.RELEASE,
                                    id = releaseId,
                                ),
                            )
                        },
                        onFilterByRecordingClick = { id ->
                            eventSink(
                                ListensUiEvent.ToggleFacet(
                                    MusicBrainzEntity(
                                        id = id,
                                        type = MusicBrainzEntityType.RECORDING,
                                    ),
                                ),
                            )
                        },
                        onSubmitMapping = { recordingMessyBrainzId, recordingId ->
                            eventSink(
                                ListensUiEvent.SubmitMapping(
                                    recordingMessyBrainzId = recordingMessyBrainzId,
                                    recordingMusicBrainzId = recordingId,
                                ),
                            )
                        },
                        onRefreshMapping = {
                            eventSink(ListensUiEvent.RefreshMapping(it))
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
