package ly.david.mbjc.ui.common.topappbar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.zIndex
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * [ScrollableTopAppBar] with filtering.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopAppBarWithFilter(
    onBack: () -> Unit = {},
    showBackButton: Boolean = true,
    resource: MusicBrainzResource? = null,
    title: String,
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,

    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,

    tabsTitles: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {},

    showFilterIcon: Boolean = true,
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = {},
    additionalActions: @Composable () -> Unit = {},
) {
    var isFilterMode by rememberSaveable { mutableStateOf(false) }

    if (isFilterMode) {
        BackHandler {
            isFilterMode = false
            onFilterTextChange("")
        }
    }

    TopAppBarWithFilterInternal(
        onBack = onBack,
        showBackButton = showBackButton,
        resource = resource,
        title = title,
        subtitle = subtitle,
        scrollBehavior = scrollBehavior,
        overflowDropdownMenuItems = overflowDropdownMenuItems,
        subtitleDropdownMenuItems = subtitleDropdownMenuItems,
        tabsTitles = tabsTitles,
        selectedTabIndex = selectedTabIndex,
        onSelectTabIndex = onSelectTabIndex,
        showFilterIcon = showFilterIcon,
        filterText = filterText,
        onFilterTextChange = onFilterTextChange,
        isFilterMode = isFilterMode,
        onFilterModeChange = { isFilterMode = it },
        additionalActions = additionalActions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarWithFilterInternal(
    onBack: () -> Unit = {},
    showBackButton: Boolean = true,
    resource: MusicBrainzResource? = null,
    title: String,
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    tabsTitles: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {},
    showFilterIcon: Boolean = true,
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = {},
    isFilterMode: Boolean = false,
    onFilterModeChange: (Boolean) -> Unit = {},
    additionalActions: @Composable () -> Unit = {},
) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box {
        AnimatedVisibility(
            visible = isFilterMode,
            modifier = Modifier.zIndex(1f),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Surface {
                Column {
                    TextField(
                        modifier = Modifier
                            .semantics { testTag = "filterTextField" }
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
                                onFilterModeChange(false)
                                onFilterTextChange("")
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.cancel))
                            }
                        },
                        placeholder = { Text(stringResource(id = R.string.filter)) },
                        trailingIcon = {
                            if (filterText.isEmpty()) return@TextField
                            IconButton(onClick = {
                                onFilterTextChange("")
                                focusRequester.requestFocus()
                            }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = stringResource(id = R.string.clear_filter)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                            }
                        ),
                        value = filterText,
                        onValueChange = {
                            onFilterTextChange(it)
                        }
                    )

                    // TODO: Filters

                    Divider()
                }
            }
        }

        ScrollableTopAppBar(
            onBack = onBack,
            showBackButton = showBackButton,
            resource = resource,
            title = title,
            subtitle = subtitle,
            scrollBehavior = scrollBehavior,
            actions = {
                if (showFilterIcon) {
                    IconButton(onClick = {
                        onFilterModeChange(true)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.filter)
                        )
                    }
                }
                additionalActions()
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
@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            TopAppBarWithFilterInternal(title = "Title")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun PreviewFilterMode() {
    PreviewTheme {
        Surface {
            TopAppBarWithFilterInternal(
                title = "Title",
                isFilterMode = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun PreviewNoSearch() {
    PreviewTheme {
        Surface {
            TopAppBarWithFilterInternal(
                title = "Title",
                showFilterIcon = false
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun PreviewWithTabs() {
    PreviewTheme {
        Surface {
            TopAppBarWithFilterInternal(
                title = "A title that is very long so that it will go off the screen and allow us to scroll.",
                tabsTitles = listOf("Tab 1", "Tab 2", "Tab 3"),
                selectedTabIndex = 1
            )
        }
    }
}
// endregion
