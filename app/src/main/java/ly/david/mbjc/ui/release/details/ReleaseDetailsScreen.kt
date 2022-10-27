package ly.david.mbjc.ui.release.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.AreaUiModel
import ly.david.data.domain.Header
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.UiModel
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaCard
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler

@Composable
internal fun ReleaseDetailsScreen(
    releaseUiModel: ReleaseUiModel?,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<UiModel>,
    onAreaClick: AreaUiModel.() -> Unit = {}
) {
    PagingLoadingAndErrorHandler(
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { uiModel: UiModel? ->
        when (uiModel) {
            is Header -> {
                releaseUiModel?.run {
                    Column {
                        formats?.let {
                            Text(text = it)
                        }
                        tracks?.let {
                            Text(text = it)
                        }
                        barcode?.let {
                            Text(text = it)
                        }
                        status?.let {
                            Text(text = it)
                        }
                        packaging?.let {
                            Text(text = it)
                        }
                        asin?.let {
                            Text(text = it)
                        }
                        quality?.let {
                            Text(text = it)
                        }
                        ListSeparatorHeader(text = stringResource(id = R.string.release_events))
                    }
                }
            }
            is AreaUiModel -> {
                AreaCard(
                    area = uiModel,
                    showType = false,
                    onAreaClick = onAreaClick
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }

    // TODO: page release events
    //  release_events not releases_events
    //  - release_id, date, area_id (though not FK?)
    //  Clicking on card goes to Area, where we will actually fetch/insert it
    //  Deleting release will delete release_event
    //  Deleting area will not delete release_event

    // TODO: should we make a single release lookup call when landing in release screen?
    //  otherwise this screen will make a 2nd call just for labels
    //  release events are always included already

    // TODO: note that having ~200 release events is an exception
    //  normally, it would just be XW, but some releases are released everywhere except a few countries
    //  So handling it in a paged list could be too much bloat
    //  Could we do something similar to web, where there's an option to see more
}
