package ly.david.ui.common.topappbar

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
import androidx.compose.material.icons.outlined.FindInPage
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.zIndex
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.ui.common.R
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

/**
 * [ScrollableTopAppBar] with filtering.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithFilter(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    showBackButton: Boolean = true,
    entity: MusicBrainzEntity? = null,
    title: String = "",
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,

    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,

    showFilterIcon: Boolean = true,
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = {},
    additionalActions: @Composable () -> Unit = {},

    additionalBar: @Composable () -> Unit = {},
) {
    var isFilterMode by rememberSaveable { mutableStateOf(false) }

    if (isFilterMode) {
        BackHandler {
            isFilterMode = false
            onFilterTextChange("")
        }
    }

    TopAppBarWithFilterInternal(
        modifier = modifier,
        onBack = onBack,
        showBackButton = showBackButton,
        entity = entity,
        title = title,
        subtitle = subtitle,
        scrollBehavior = scrollBehavior,
        overflowDropdownMenuItems = overflowDropdownMenuItems,
        subtitleDropdownMenuItems = subtitleDropdownMenuItems,
        showFilterIcon = showFilterIcon,
        filterText = filterText,
        onFilterTextChange = onFilterTextChange,
        isFilterMode = isFilterMode,
        onFilterModeChange = { isFilterMode = it },
        additionalActions = additionalActions,
        additionalBar = additionalBar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopAppBarWithFilterInternal(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    showBackButton: Boolean = true,
    entity: MusicBrainzEntity? = null,
    title: String = "",
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    showFilterIcon: Boolean = true,
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = {},
    isFilterMode: Boolean = false,
    onFilterModeChange: (Boolean) -> Unit = {},
    additionalActions: @Composable () -> Unit = {},
    additionalBar: @Composable () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier) {
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
                            .testTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
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
                            IconButton(
                                onClick = {
                                    onFilterModeChange(false)
                                    onFilterTextChange("")
                                },
                                modifier = Modifier.testTag(TopAppBarWithFilterTestTag.FILTER_BACK.name)
                            ) {
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
            entity = entity,
            title = title,
            subtitle = subtitle,
            scrollBehavior = scrollBehavior,
            actions = {
                if (showFilterIcon) {
                    IconButton(onClick = {
                        onFilterModeChange(true)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.FindInPage,
                            contentDescription = stringResource(id = R.string.filter)
                        )
                    }
                }
                additionalActions()
            },
            overflowDropdownMenuItems = overflowDropdownMenuItems,
            subtitleDropdownMenuItems = subtitleDropdownMenuItems,
            additionalBar = additionalBar
        )
    }
}

enum class TopAppBarWithFilterTestTag {
    FILTER_TEXT_FIELD,
    FILTER_BACK,
}

// region Previews
@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun Default() {
    PreviewTheme {
        TopAppBarWithFilterInternal(title = "Title")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun FilterMode() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            title = "Title",
            isFilterMode = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun NoFilter() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            title = "Title",
            showFilterIcon = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun WithTabs() {
    PreviewTheme {
        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

        TopAppBarWithFilterInternal(
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            additionalBar = {
                TabsBar(
                    tabsTitle = listOf("Tab 1", "Tab 2", "Tab 3"),
                    selectedTabIndex = selectedTabIndex,
                    onSelectTabIndex = { selectedTabIndex = it }
                )
            }
        )
    }
}
// endregion
