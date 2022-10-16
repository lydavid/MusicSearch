package ly.david.mbjc.ui.common.topappbar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import ly.david.mbjc.R
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun TopAppBarWithSearch(
    onBack: () -> Unit = {},
    openDrawer: (() -> Unit)? = null,
    resource: MusicBrainzResource? = null,
    title: String,
    subtitle: String = "",
    showSearchIcon: Boolean = true,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    searchText: String = "",
    onSearchTextChange: (String) -> Unit = {},
    tabsTitles: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {}
) {
    var isSearchAndFilterMode by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // TODO: expand out from the icon
    AnimatedVisibility(
        visible = isSearchAndFilterMode,
        enter = expandVertically(),
        exit = shrinkVertically { fullHeight -> fullHeight/2 }
    ) {

        // TODO: when returning, focus is in front of search text
        //  most apps seems to not bring up the keyboard when returning
        LaunchedEffect(Unit) {
            // TODO: only do them when first clicking on search icon
            focusRequester.requestFocus()
        }

        BackHandler {
            isSearchAndFilterMode = false
            onSearchTextChange("")
        }

        Surface {
            Column {
                TextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth(),
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        IconButton(onClick = {
                            isSearchAndFilterMode = false
                            onSearchTextChange("")
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.exit_filter))
                        }
                    },
                    placeholder = { Text(stringResource(id = R.string.search)) },
                    trailingIcon = {
                        if (searchText.isEmpty()) return@TextField
                        IconButton(onClick = {
                            onSearchTextChange("")
                            focusRequester.requestFocus()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = stringResource(id = R.string.clear_filter))
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    ),
                    value = searchText,
                    onValueChange = {
                        onSearchTextChange(it)
                    }
                )

                // TODO: Filters

                Divider()
            }
        }
    }

    AnimatedVisibility(
        visible = !isSearchAndFilterMode,
        enter = expandVertically { fullHeight -> fullHeight/2 },
        exit = shrinkVertically()
    ) {
        ScrollableTopAppBar(
            onBack = onBack,
            openDrawer = openDrawer,
            resource = resource,
            title = title,
            subtitle = subtitle,
            mainAction = {
                if (showSearchIcon) {
                    IconButton(onClick = {
                        isSearchAndFilterMode = true
                    }) {
                        Icon(
                            Icons.Default.Search,
                            stringResource(id = R.string.filter)
                        )
                    }
                }
            },
            overflowDropdownMenuItems = overflowDropdownMenuItems,
            subtitleDropdownMenuItems = subtitleDropdownMenuItems,
            tabsTitles = tabsTitles,
            selectedTabIndex = selectedTabIndex,
            onSelectTabIndex = onSelectTabIndex
        )
    }
}

// region Previews
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            // TODO: Low: animation crashes in interactive mode
            TopAppBarWithSearch(title = "Title")
        }
    }
}

@DefaultPreviews
@Composable
private fun PreviewNoSearch() {
    PreviewTheme {
        Surface {
            TopAppBarWithSearch(
                title = "Title",
                showSearchIcon = false
            )
        }
    }
}

@DefaultPreviews
@Composable
private fun PreviewWithTabs() {
    PreviewTheme {
        Surface {
            TopAppBarWithSearch(
                title = "A title that is very long so that it will go off the screen and allow us to scroll.",
                tabsTitles = listOf("Tab 1", "Tab 2", "Tab 3"),
                selectedTabIndex = 1
            )
        }
    }
}
// endregion
