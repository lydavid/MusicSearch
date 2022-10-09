package ly.david.mbjc.ui.area

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.mbjc.R
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.area.relations.AreaRelationsScreen
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AreaScaffold(
    areaId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: AreaViewModel = hiltViewModel(),
) {

    val resource = MusicBrainzResource.AREA

    var title by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val relationsLazyListState = rememberLazyListState()
    var pagedRelations: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
    val relationsLazyPagingItems: LazyPagingItems<UiModel> = rememberFlowWithLifecycleStarted(pagedRelations)
        .collectAsLazyPagingItems()

    var recordedLookup by rememberSaveable { mutableStateOf(false) }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        title = titleWithDisambiguation
    }

    LaunchedEffect(key1 = title) {
        if (title.isEmpty()) return@LaunchedEffect

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = areaId,
                resource = resource,
                summary = title
            )
            recordedLookup = true
        }
    }

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.open_in_browser)) },
                        onClick = {
                            context.lookupInBrowser(resource, areaId)
                            closeMenu()
                        }
                    )
                },
            )
        },
    ) { innerPadding ->
        AreaRelationsScreen(
            modifier = Modifier.padding(innerPadding),
            areaId = areaId,
            onItemClick = onItemClick,
            lazyListState = relationsLazyListState,
            lazyPagingItems = relationsLazyPagingItems,
            onPagedRelationsChange = {
                pagedRelations = it
            },
            onAreaLookup = {
                if (titleWithDisambiguation.isNullOrEmpty()) {
                    title = it.getNameWithDisambiguation()
                }
            }
        )
    }
}
