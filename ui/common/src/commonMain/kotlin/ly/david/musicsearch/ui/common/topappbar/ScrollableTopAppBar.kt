package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.icons.ArrowBack
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.MoreVert
import ly.david.musicsearch.ui.common.theme.LocalStrings

/**
 * Assuming an average api call finishes under 300ms, we should delay showing the loading indicator until we
 * exceed this time.
 */
private const val DELAY_LOADING_MS = 300L

/**
 * [TopAppBar] with icon for [entity], scrollable [title]/[subtitle];
 * back button if [showBackButton], invoking [onBack].
 *
 * @param entity What [MusicBrainzEntity]'s icon to display.
 * @param actions Actions displayed in a [RowScope].
 * @param overflowDropdownMenuItems If set, displays three-ellipses action button at the end of the bar.
 *  When clicked, the items in this composable will be displayed in a dropdown menu.
 * @param additionalBar Additional control bar under the title/subtitle.
 *  This is where we can slot in tabs/chips.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableTopAppBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    showBackButton: Boolean = true,
    entity: MusicBrainzEntity? = null,
    title: String = "",
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,
    isEditMode: Boolean = false,

    actions: @Composable () -> Unit = {},

    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,

    additionalBar: @Composable () -> Unit = {},
) {
    val strings = LocalStrings.current

    Column(modifier = modifier) {
        TopAppBar(
            title = {
                TitleAndSubtitle(
                    title = title,
                    entity = entity,
                    subtitle = subtitle,
                    subtitleDropdownMenuItems = subtitleDropdownMenuItems,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if (isEditMode) MaterialTheme.colorScheme.surfaceVariant else Color.Unspecified,
            ),
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = CustomIcons.ArrowBack,
                            contentDescription = strings.back,
                        )
                    }
                }
            },
            actions = {
                actions()
                OverflowMenu(overflowDropdownMenuItems = overflowDropdownMenuItems)
            },
        )

        additionalBar()
    }
}

@Composable
private fun TitleAndSubtitle(
    title: String,
    entity: MusicBrainzEntity? = null,
    subtitle: String = "",
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
) {
    var showLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = title) {
        showLoading = if (title.isEmpty()) {
            delay(DELAY_LOADING_MS)
            true
        } else {
            false
        }
    }

    if (showLoading) {
        DotsFlashing()
    } else {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                if (entity != null) {
                    EntityIcon(
                        entity = entity,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
                SelectionContainer {
                    Text(text = title)
                }
            }
            if (subtitle.isNotEmpty()) {
                SubtitleWithOverflow(
                    subtitle = subtitle,
                    subtitleDropdownMenuItems = subtitleDropdownMenuItems,
                )
            }
        }
    }
}

@Composable
private fun SubtitleWithOverflow(
    subtitle: String = "",
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    val scope = remember {
        object : OverflowMenuScope {
            override fun closeMenu() {
                showMenu = false
            }
        }
    }

    if (subtitleDropdownMenuItems != null) {
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            content = {
                Column {
                    subtitleDropdownMenuItems.invoke(scope)
                }
            },
        )
    }

    SelectionContainer {
        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                // As long as tap and scroll does not trigger a click, we can use this
                // as a way to "navigate up".
                // When a user searches for a release group, they can't return to the artist by
                // pressing back.
                // This will allow them to click the subtitle to go to the artist's page.
                // From release screen, the subtitle is usually "Release by [Artist]".
                // MB website has "(see all versions of this release, 6 available)" to allow up navigation
                // to release group screen.
                .clickable {
                    showMenu = !showMenu
                }
                .semantics {
                    testTag = "TopBarSubtitle"
                },
        )
    }
}

@Composable
private fun OverflowMenu(
    overflowDropdownMenuItems: (@Composable OverflowMenuScope.() -> Unit)? = null,
) {
    val strings = LocalStrings.current
    var showMenu by rememberSaveable { mutableStateOf(false) }

    val scope = remember {
        object : OverflowMenuScope {
            override fun closeMenu() {
                showMenu = false
            }
        }
    }

    if (overflowDropdownMenuItems != null) {
        Box {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = CustomIcons.MoreVert,
                    contentDescription = strings.moreActions,
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                content = {
                    // We lose out on the ability to control these items within a Column,
                    // but now each item can close itself.
                    Column {
                        overflowDropdownMenuItems.invoke(scope)
                    }
                },
            )
        }
    }
}
