package ly.david.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

// region Preview
@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun Default() {
    PreviewTheme {
        ScrollableTopAppBar(
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen.",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun WithIcon() {
    PreviewTheme {
        ScrollableTopAppBar(
            entity = MusicBrainzEntity.ARTIST,
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            subtitle = "A subtitle that is also very long that will also go off the screen.",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DefaultPreviews
@Composable
private fun WithTabs() {
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
// endregion
