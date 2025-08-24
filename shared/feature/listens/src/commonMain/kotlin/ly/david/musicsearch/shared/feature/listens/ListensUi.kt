package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icons.Album
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter

private const val SPACER_WEIGHT = 0.1f
private const val CONTENT_WEIGHT = 0.8f

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
        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
            ) {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    IconButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = { showDialog = false },
                    ) {
                        Icon(
                            imageVector = CustomIcons.Clear,
                            contentDescription = "Close",
                        )
                    }
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
                    BottomSheetContent(
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
            ) { listenListItemModel: ListenListItemModel? ->
                listenListItemModel?.let {
                    ListenListItem(
                        listen = it,
                        onClick = { id ->
                            eventSink(
                                ListensUiEvent.ClickItem(
                                    entity = MusicBrainzEntityType.RECORDING,
                                    id = id,
                                ),
                            )
                        },
                        onClickMoreActions = {
                            showBottomSheetForListen = it
                        },
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
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(SPACER_WEIGHT))

        Column(
            modifier = Modifier
                .weight(CONTENT_WEIGHT)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                buildAnnotatedString {
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
                textAlign = TextAlign.Center,
            )
            TextField(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                value = text,
                label = { Text(strings.username) },
                placeholder = { Text(strings.enterUsername) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                trailingIcon = {
                    if (text.isEmpty()) return@TextField
                    IconButton(onClick = {
                        onTextChange("")
                        focusRequester.requestFocus()
                    }) {
                        Icon(
                            CustomIcons.Clear,
                            contentDescription = "Clear",
                        )
                    }
                },
                onValueChange = { newText ->
                    if (!newText.contains("\n")) {
                        onTextChange(newText)
                    }
                },
            )
            Button(
                modifier = Modifier
                    .padding(top = 16.dp),
                onClick = onSetUsername,
            ) {
                Text(strings.set)
            }
        }
        Spacer(modifier = Modifier.weight(SPACER_WEIGHT))
    }
}

@Composable
internal fun BottomSheetContent(
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
