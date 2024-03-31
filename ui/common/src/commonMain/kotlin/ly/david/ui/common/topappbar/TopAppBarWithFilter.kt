package ly.david.ui.common.topappbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.zIndex
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings

/**
 * [ScrollableTopAppBar] with filtering.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun TopAppBarWithFilter(
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
)

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
    val strings = LocalStrings.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = isFilterMode,
            modifier = Modifier.zIndex(1f),
            enter = expandVertically(),
            exit = shrinkVertically(),
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
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    onFilterModeChange(false)
                                    onFilterTextChange("")
                                },
                                modifier = Modifier.testTag(TopAppBarWithFilterTestTag.FILTER_BACK.name),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = strings.cancel,
                                )
                            }
                        },
                        placeholder = { Text(strings.filter) },
                        trailingIcon = {
                            if (filterText.isEmpty()) return@TextField
                            IconButton(onClick = {
                                onFilterTextChange("")
                                focusRequester.requestFocus()
                            }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = strings.clearFilter,
                                )
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            },
                        ),
                        value = filterText,
                        onValueChange = {
                            onFilterTextChange(it)
                        },
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
                            contentDescription = strings.filter,
                        )
                    }
                }
                additionalActions()
            },
            overflowDropdownMenuItems = overflowDropdownMenuItems,
            subtitleDropdownMenuItems = subtitleDropdownMenuItems,
            additionalBar = additionalBar,
        )
    }
}

enum class TopAppBarWithFilterTestTag {
    FILTER_TEXT_FIELD,
    FILTER_BACK,
}
