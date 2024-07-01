package ly.david.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewScrollableTopAppBar() {
    PreviewTheme {
        ScrollableTopAppBar(
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen.",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewScrollableTopAppBarWithIcon() {
    PreviewTheme {
        ScrollableTopAppBar(
            entity = MusicBrainzEntity.ARTIST,
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen.",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewScrollableTopAppBarWithTabs() {
    PreviewTheme {
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

        ScrollableTopAppBar(
            entity = MusicBrainzEntity.RELEASE_GROUP,
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen.",
            additionalBar = {
                TabsBar(
                    tabsTitle = listOf(
                        "Tab 1",
                        "Tab 2",
                        "Tab 3",
                    ),
                    selectedTabIndex = selectedTabIndex,
                    onSelectTabIndex = { selectedTabIndex = it },
                )
            },
        )
    }
}
