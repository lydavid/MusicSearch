package ly.david.mbjc.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
fun ScrollableTopAppBar(
    title: String,
    subtitle: String = "",

    onBack: () -> Unit = {},
    openDrawer: (() -> Unit)? = null,

    mainAction: @Composable (() -> Unit)? = null,
    dropdownMenuItems: @Composable (ColumnScope.() -> Unit)? = null,

    // TODO: Can we split these concerns somehow?
    tabsTitle: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {}
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    Column {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = title,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    )
                    if (subtitle.isNotEmpty()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }
                }
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back to previous screen.")
                    } else {
                        Icon(Icons.Default.Menu, contentDescription = "Open navigation drawer.")
                    }
                }
            },
            actions = {

                mainAction?.invoke()

                if (dropdownMenuItems != null) {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More actions.")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        content = dropdownMenuItems
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.background
        )

        TabsBar(
            tabsTitle = tabsTitle,
            selectedTabIndex = selectedTabIndex,
            onSelectTabIndex = onSelectTabIndex
        )
    }
}

@Composable
fun TabsBar(
    tabsTitle: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {}
) {
    if (tabsTitle.isNotEmpty()) {
        ScrollableTabRow(
            backgroundColor = MaterialTheme.colors.background,
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardPreview() {
    MusicBrainzJetpackComposeTheme {
        ScrollableTopAppBar("A title", "A subtitle")
    }
}
