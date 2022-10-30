package ly.david.mbjc.ui.release.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.AreaUiModel
import ly.david.data.domain.Header
import ly.david.data.domain.LabelUiModel
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.UiModel
import ly.david.data.getDisplayTypes
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaCard
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.label.LabelCard

@Composable
internal fun ReleaseDetailsScreen(
    releaseUiModel: ReleaseUiModel?,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<UiModel>,
    onLabelClick: LabelUiModel.() -> Unit = {},
    onAreaClick: AreaUiModel.() -> Unit = {},
    viewModel: ReleaseDetailsViewModel = hiltViewModel()
) {

    var releaseLength: String? by rememberSaveable { mutableStateOf(null) }

    LaunchedEffect(key1 = releaseUiModel) {
        if (releaseUiModel == null) return@LaunchedEffect
        releaseLength = viewModel.getFormattedReleaseLength(releaseUiModel.id)
    }

    // TODO: rather than page on this screen, can we just pass in all release events?
    PagingLoadingAndErrorHandler(
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { uiModel: UiModel? ->
        when (uiModel) {
            is Header -> {
                releaseUiModel?.run {
                    SelectionContainer {
                        Column {
                            ListSeparatorHeader(text = stringResource(id = R.string.release_information))
                            barcode?.let {
                                TextWithHeading(headingRes = R.string.barcode, text = it)
                            }
                            formats?.let {
                                TextWithHeading(headingRes = R.string.format, text = it)
                            }
                            tracks?.let {
                                TextWithHeading(headingRes = R.string.tracks, text = it)
                            }
                            releaseLength?.let {
                                TextWithHeading(headingRes = R.string.length, text = it)
                            }

                            ListSeparatorHeader(text = stringResource(id = R.string.additional_details))
                            releaseGroup?.let {
                                TextWithHeading(headingRes = R.string.type, text = it.getDisplayTypes())
                            }
                            packaging?.let {
                                TextWithHeading(headingRes = R.string.packaging, text = it)
                            }
                            status?.let {
                                TextWithHeading(headingRes = R.string.status, text = it)
                            }
                            // TODO: language : need to convert abbr to text
                            // TODO: script
                            quality?.let {
                                TextWithHeading(headingRes = R.string.data_quality, text = it)
                            }
                            asin?.let {
                                TextWithHeading(headingRes = R.string.asin, text = it)
                            }

                            if (labels.isNotEmpty()) {
                                ListSeparatorHeader(text = stringResource(id = R.string.labels))
                                labels.forEach {
                                    LabelCard(label = it, onLabelClick = onLabelClick)
                                }
                            }

                            ListSeparatorHeader(text = stringResource(id = R.string.release_events))
                        }
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
}
