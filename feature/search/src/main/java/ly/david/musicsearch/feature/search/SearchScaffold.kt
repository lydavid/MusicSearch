package ly.david.musicsearch.feature.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.feature.search.internal.SearchScreen
import ly.david.ui.common.R
import ly.david.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScaffold(
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    initialQuery: String? = null,
    initialEntity: MusicBrainzEntity? = null,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = stringResource(id = R.string.search_musicbrainz),
            )
        },
    ) { innerPadding ->
        SearchScreen(
            modifier = Modifier.padding(innerPadding),
            snackbarHostState = snackbarHostState,
            onItemClick = onItemClick,
            initialQuery = initialQuery,
            initialEntity = initialEntity
        )
    }
}
