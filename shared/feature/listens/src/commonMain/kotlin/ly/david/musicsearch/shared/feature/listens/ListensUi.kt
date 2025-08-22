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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.theme.LocalStrings
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
                        text = state.textFieldText,
                        strings = strings,
                        onTextChange = {
                            eventSink(ListensUiEvent.EditText(it))
                        },
                        onSetUsername = {
                            eventSink(ListensUiEvent.SetUsername)
                        },
                    )
                }
            }
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
                title = strings.listens,
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = {
                            Text("Change username")
                        },
                        onClick = {
                            showDialog = true
                            closeMenu()
                        },
                    )
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

        if (state.username.isEmpty()) {
            UsernameInput(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
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
            ScreenWithPagingLoadingAndError(
                lazyPagingItems = state.listensPagingDataFlow.collectAsLazyPagingItems(),
                modifier = Modifier
                    .padding(innerPadding),
                lazyListState = state.lazyListState,
                keyed = true,
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
                    )
                }
            }
        }
    }
}

@Composable
private fun UsernameInput(
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
                            "https://listenbrainz.org",
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
                            contentDescription = strings.clearSearch,
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
                Text("Set")
            }
        }
        Spacer(modifier = Modifier.weight(SPACER_WEIGHT))
    }
}
