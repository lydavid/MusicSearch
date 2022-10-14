package ly.david.mbjc.ui.common.topappbar

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Assuming an average api call finishes under 300ms, we should delay showing the loading indicator until we
 * exceed this time.
 */
private const val DELAY_LOADING_MS = 300L

/**
 *
 * @param resource What [MusicBrainzResource]'s icon to display.
 */
@Composable
internal fun ScrollableTopAppBar(
    onBack: () -> Unit = {},
    openDrawer: (() -> Unit)? = null,
    resource: MusicBrainzResource? = null,
    title: String,
    subtitle: String = "",
    mainAction: @Composable (() -> Unit)? = null,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,

    // TODO: Can we split these concerns somehow?
    tabsTitles: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {}
) {

    Column {
        SmallTopAppBar(
            title = {
                TitleBar(
                    resource = resource,
                    title = title,
                    subtitle = subtitle,
                    subtitleDropdownMenuItems = subtitleDropdownMenuItems
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    if (openDrawer == null) {
                        onBack()
                    } else {
                        openDrawer.invoke()
                    }
                }) {
                    if (openDrawer == null) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    } else {
                        Icon(Icons.Default.Menu, contentDescription = "Open navigation drawer")
                    }
                }
            },
            actions = {
                mainAction?.invoke()
                OverflowMenu(overflowDropdownMenuItems = overflowDropdownMenuItems)
            }
        )

        TabsBar(
            tabsTitle = tabsTitles,
            selectedTabIndex = selectedTabIndex,
            onSelectTabIndex = onSelectTabIndex
        )
    }
}

@Composable
private fun TitleBar(
    resource: MusicBrainzResource? = null,
    title: String,
    subtitle: String = "",
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
) {

    // TODO: can we generalize adding a dropdown menu to a clickable composable?
    //  this was copy/pasted from OverflowMenu
    var showMenu by rememberSaveable { mutableStateOf(false) }

    val scope = remember {
        object : OverflowMenuScope {
            override fun closeMenu() {
                showMenu = false
            }
        }
    }

    var showLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = title) {
        showLoading = if (title.isEmpty()) {
            delay(DELAY_LOADING_MS)
            true
        } else {
            false
        }
    }

    if (subtitleDropdownMenuItems != null) {
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            content = {
                // We lose out on the ability to control these items within a Column,
                // but now each item can close itself.
                Column {
                    subtitleDropdownMenuItems.invoke(scope)
                }
            }
        )
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
                Text(text = title)
            }
            if (subtitle.isNotEmpty()) {
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
                )
            }
        }
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
            Icon(Icons.Default.MoreVert, contentDescription = "More actions.")
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

@Composable
private fun TabsBar(
    tabsTitle: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {}
) {
    if (tabsTitle.isNotEmpty()) {
        ScrollableTabRow(
//            backgroundColor = MaterialTheme.colors.background,
            selectedTabIndex = selectedTabIndex
        ) {
            tabsTitle.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = {
                        onSelectTabIndex(index)
                    }
                )
            }
        }
    }
}

// region Preview
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScrollableTopAppBarPreview() {
    PreviewTheme {
        ScrollableTopAppBar(
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen."
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScrollableTopAppBarIconPreview() {
    PreviewTheme {
        ScrollableTopAppBar(
            resource = MusicBrainzResource.ARTIST,
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen."
        )
    }
}
// endregion
