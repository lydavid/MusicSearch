package ly.david.mbjc.ui.instrument

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.InstrumentUiModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun InstrumentScreen(
    modifier: Modifier = Modifier,
    instrumentId: String,
    onTitleUpdate: (title: String) -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: InstrumentViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var instrument: InstrumentUiModel? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = instrumentId) {
        try {
            instrument = viewModel.lookupInstrument(instrumentId)
            onTitleUpdate(instrument?.getNameWithDisambiguation() ?: "[should not happen]")
        } catch (ex: Exception) {
            onTitleUpdate("[Instrument lookup failed]")
        }
        lookupInProgress = false
    }

    val lazyPagingItems: LazyPagingItems<ly.david.data.domain.UiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
            .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        somethingElseLoading = lookupInProgress,
        lazyListState = lazyListState,
    ) { uiModel: ly.david.data.domain.UiModel? ->

        when (uiModel) {
            is ly.david.data.domain.RelationUiModel -> {
                RelationCard(
                    relation = uiModel,
                    onItemClick = onItemClick,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
