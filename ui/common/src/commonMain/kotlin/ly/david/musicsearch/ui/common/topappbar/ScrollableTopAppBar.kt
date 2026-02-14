package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.icons.ArrowBack
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.MoreVert
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.back
import musicsearch.ui.common.generated.resources.moreActions
import org.jetbrains.compose.resources.stringResource

/**
 * Assuming an average api call finishes under 300ms, we should delay showing the loading indicator until we
 * exceed this time.
 */
private const val DELAY_LOADING_MS = 300L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableTopAppBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    showBackButton: Boolean = true,
    entity: MusicBrainzEntityType? = null,
    title: String = "",
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,
    containerColor: Color = Color.Unspecified,
    actions: @Composable () -> Unit = {},
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    additionalBar: @Composable ColumnScope.() -> Unit = {},
) {
    ScrollableTopAppBar(
        modifier = modifier,
        onBack = onBack,
        showBackButton = showBackButton,
        entity = entity,
        annotatedString = AnnotatedString(title),
        subtitle = subtitle,
        scrollBehavior = scrollBehavior,
        containerColor = containerColor,
        actions = actions,
        overflowDropdownMenuItems = overflowDropdownMenuItems,
        subtitleDropdownMenuItems = subtitleDropdownMenuItems,
        additionalBar = additionalBar,
    )
}

/**
 * [TopAppBar] with icon for [entity], scrollable [title]/[subtitle];
 * back button if [showBackButton], invoking [onBack].
 *
 * @param entity What [MusicBrainzEntityType]'s icon to display.
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
    entity: MusicBrainzEntityType? = null,
    annotatedString: AnnotatedString = AnnotatedString(""),
    subtitle: String = "",
    scrollBehavior: TopAppBarScrollBehavior? = null,
    containerColor: Color = Color.Unspecified,
    actions: @Composable (() -> Unit) = {},
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    additionalBar: @Composable (ColumnScope.() -> Unit) = {},
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                TitleAndSubtitle(
                    annotatedString = annotatedString,
                    entity = entity,
                    subtitle = subtitle,
                    subtitleDropdownMenuItems = subtitleDropdownMenuItems,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
            ),
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = CustomIcons.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
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
    annotatedString: AnnotatedString,
    entity: MusicBrainzEntityType? = null,
    subtitle: String = "",
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
) {
    var showLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = annotatedString) {
        showLoading = if (annotatedString.text.isEmpty()) {
            delay(DELAY_LOADING_MS)
            true
        } else {
            false
        }
    }

    if (showLoading) {
        DotsFlashing()
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.semantics {
                isTraversalGroup = true
            },
        ) {
            if (entity != null) {
                EntityIcon(
                    entityType = entity,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
            Column {
                SelectionContainer {
                    Text(
                        text = annotatedString,
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                    )
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
                    contentDescription = stringResource(Res.string.moreActions),
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
