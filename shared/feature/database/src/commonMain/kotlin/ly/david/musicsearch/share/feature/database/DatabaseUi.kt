package ly.david.musicsearch.share.feature.database

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.getNamePlural
import ly.david.musicsearch.ui.common.screen.AllEntitiesScreen
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.HistoryScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DatabaseUi(
    state: DatabaseUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val strings = LocalStrings.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = strings.database,
                topAppBarFilterState = state.topAppBarFilterState,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismissBox(
                    state = rememberSwipeToDismissBoxState(),
                    backgroundContent = {},
                    content = { Snackbar(snackbarData) },
                )
            }
        },
    ) { innerPadding ->
        DatabaseUi(
            state = state,
            onDestinationClick = {
                eventSink(DatabaseUiEvent.GoToScreen(it))
            },
            modifier = Modifier
                .padding(innerPadding),
        )
    }
}

@Composable
internal fun DatabaseUi(
    state: DatabaseUiState,
    onDestinationClick: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val filterText = state.topAppBarFilterState.filterText
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(state = scrollState),
    ) {
        strings.history.run {
            if (this.contains(filterText, ignoreCase = true)) {
                ClickableItem(
                    title = this,
                    endIcon = Icons.Default.ChevronRight,
                    onClick = { onDestinationClick(HistoryScreen) },
                )
            }
        }

        strings.images.run {
            if (this.contains(filterText, ignoreCase = true)) {
                ClickableItem(
                    title = this,
                    endIcon = Icons.Default.ChevronRight,
                    onClick = {
                        onDestinationClick(CoverArtsScreen())
                    },
                )
            }
        }

        MusicBrainzEntity.entries
            .filterNot { it == MusicBrainzEntity.COLLECTION || it == MusicBrainzEntity.URL }
            .forEach { entity ->
                val title = entity.getNamePlural(strings)
                if (title.contains(filterText, ignoreCase = true)) {
                    ClickableItem(
                        title = title,
                        subtitle = (state.entitiesCount[entity] ?: 0).toString(),
                        endIcon = Icons.Default.ChevronRight,
                        onClick = { onDestinationClick(AllEntitiesScreen(entity = entity)) },
                    )
                }
            }
    }
}
