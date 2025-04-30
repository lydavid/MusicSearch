package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.zIndex
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.icons.ArrowBack
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.FindInPage
import ly.david.musicsearch.ui.core.LocalStrings

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

    topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    topAppBarEditState: TopAppBarEditState = TopAppBarEditState(),

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

    topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    topAppBarEditState: TopAppBarEditState = TopAppBarEditState(),

    additionalActions: @Composable () -> Unit = {},
    additionalBar: @Composable () -> Unit = {},
) {
    val strings = LocalStrings.current

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = topAppBarFilterState.isFilterMode,
            modifier = Modifier.zIndex(1f),
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(
                    TextFieldValue(topAppBarFilterState.filterText, TextRange(topAppBarFilterState.filterText.length)),
                )
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Surface(
                modifier = Modifier.padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                ),
            ) {
                Column {
                    TextField(
                        modifier = Modifier
                            .testTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
                            .focusRequester(focusRequester)
                            .fillMaxWidth(),
                        maxLines = 1,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    topAppBarFilterState.dismiss()
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
                        trailingIcon = {
                            if (topAppBarFilterState.filterText.isEmpty()) return@TextField
                            IconButton(onClick = {
                                topAppBarFilterState.clear()
                                textFieldValue = TextFieldValue()
                                focusRequester.requestFocus()
                            }) {
                                Icon(
                                    CustomIcons.Clear,
                                    contentDescription = strings.clearFilter,
                                )
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            },
                        ),
                        value = textFieldValue,
                        onValueChange = {
                            // Recomposition after toggling filter mode may reset the previous value otherwise
                            val newValue = if (topAppBarFilterState.isFilterMode) it else TextFieldValue("")
                            textFieldValue = newValue
                            topAppBarFilterState.updateFilterText(newValue.text)
                        },
                    )

                    Divider()
                }
            }
        }

        ScrollableTopAppBar(
            onBack = {
                if (topAppBarEditState.isEditMode) {
                    topAppBarEditState.dismiss()
                } else {
                    onBack()
                }
            },
            showBackButton = showBackButton,
            entity = entity,
            title = if (topAppBarEditState.isEditMode) {
                "Editing..."
            } else {
                title
            },
            subtitle = subtitle,
            scrollBehavior = scrollBehavior,
            isEditMode = topAppBarEditState.isEditMode,
            actions = {
                if (topAppBarFilterState.show) {
                    IconButton(onClick = {
                        topAppBarFilterState.toggleFilterMode(true)
                    }) {
                        Icon(
                            imageVector = CustomIcons.FindInPage,
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
