package ly.david.mbjc.ui.label

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ly.david.mbjc.R
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.label.relations.LabelRelationsScreen
import ly.david.mbjc.ui.navigation.Destination

private enum class LabelTab(@StringRes val titleRes: Int) {
    RELEASES(R.string.releases),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabelScaffold(
    labelId: String,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
) {

    var titleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(LabelTab.RELEASES) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = MusicBrainzResource.LABEL,
                title = titleState,
                onBack = onBack,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.open_in_browser)) },
                        onClick = {
                            context.lookupInBrowser(MusicBrainzResource.LABEL, labelId)
                            closeMenu()
                        }
                    )
                },
                tabsTitles = LabelTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = LabelTab.values()[it] }
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            LabelTab.RELEASES -> {
                Text(text = "releases")
            }
            LabelTab.RELATIONSHIPS -> {
                LabelRelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    labelId = labelId,
                    onTitleUpdate = { title ->
                        titleState = title
                    },
                    onItemClick = onItemClick
                )
            }
            LabelTab.STATS -> {
                Text(text = "stats")
            }
        }
    }
}
