package ly.david.ui.common.topappbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.R
import ly.david.ui.common.ResourceIcon
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

/**
 * Assuming an average api call finishes under 300ms, we should delay showing the loading indicator until we
 * exceed this time.
 */
private const val DELAY_LOADING_MS = 300L

/**
 * [TopAppBar] with icon for [resource], scrollable [title]/[subtitle];
 * back button if [showBackButton], invoking [onBack].
 *
 * @param resource What [MusicBrainzResource]'s icon to display.
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
    resource: MusicBrainzResource? = null,
    title: String = "",
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,

    actions: @Composable () -> Unit = {},

    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,

    additionalBar: @Composable () -> Unit = {},
) {

    Column(modifier = modifier) {
        TopAppBar(
            title = {
                TitleAndSubtitle(
                    title = title,
                    resource = resource,
                    subtitle = subtitle,
                    subtitleDropdownMenuItems = subtitleDropdownMenuItems
                )
            },
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                }
            },
            actions = {
                actions()
                OverflowMenu(overflowDropdownMenuItems = overflowDropdownMenuItems)
            }
        )

        additionalBar()
    }
}

@Composable
private fun TitleAndSubtitle(
    title: String,
    resource: MusicBrainzResource? = null,
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
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                if (resource != null) {
                    ResourceIcon(
                        resource = resource,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                SelectionContainer {
                    Text(text = title)
                }
            }
            if (subtitle.isNotEmpty()) {
                SubtitleWithOverflow(
                    subtitle = subtitle,
                    subtitleDropdownMenuItems = subtitleDropdownMenuItems
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
            }
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
                }
        )
    }
}

@Composable
private fun OverflowMenu(
    overflowDropdownMenuItems: (@Composable OverflowMenuScope.() -> Unit)? = null
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    val scope = remember {
        object : OverflowMenuScope {
            override fun closeMenu() {
                showMenu = false
            }
        }
    }

    if (overflowDropdownMenuItems != null) {
        IconButton(onClick = { showMenu = !showMenu }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.more_actions)
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
            }
        )
    }
}

// region Preview
@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun Default() {
    PreviewTheme {
        ScrollableTopAppBar(
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen."
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun WithIcon() {
    PreviewTheme {
        ScrollableTopAppBar(
            resource = MusicBrainzResource.ARTIST,
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen."
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun WithTabs() {
    PreviewTheme {
        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

        ScrollableTopAppBar(
            resource = MusicBrainzResource.RELEASE_GROUP,
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen.",
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
