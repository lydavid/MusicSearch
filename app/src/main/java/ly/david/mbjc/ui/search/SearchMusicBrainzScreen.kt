package ly.david.mbjc.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.data.domain.AreaListItemModel
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.domain.EndOfList
import ly.david.data.domain.EventListItemModel
import ly.david.data.domain.InstrumentListItemModel
import ly.david.data.domain.LabelListItemModel
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.SeriesListItemModel
import ly.david.data.domain.WorkListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.searchableResources
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaListItem
import ly.david.mbjc.ui.artist.ArtistListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.event.EventListItem
import ly.david.mbjc.ui.instrument.InstrumentListItem
import ly.david.mbjc.ui.label.LabelListItem
import ly.david.mbjc.ui.place.PlaceListItem
import ly.david.mbjc.ui.recording.RecordingListItem
import ly.david.mbjc.ui.release.ReleaseListItem
import ly.david.mbjc.ui.releasegroup.ReleaseGroupListItem
import ly.david.mbjc.ui.series.SeriesListItem
import ly.david.mbjc.ui.work.WorkListItem
import ly.david.ui.common.dialog.SimpleAlertDialog

@Composable
internal fun SearchMusicBrainzScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    searchQuery: String? = null,
    searchOption: MusicBrainzResource? = null,
    viewModel: SearchMusicBrainzViewModel = hiltViewModel()
) {

    val lazyPagingItems: LazyPagingItems<ListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.searchResultsListItemModel)
            .collectAsLazyPagingItems()

    var text by rememberSaveable { mutableStateOf("") }
    var selectedOption by rememberSaveable { mutableStateOf(MusicBrainzResource.ARTIST) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    if (showAlertDialog) {
        SimpleAlertDialog(
            title = stringResource(id = R.string.search_cannot_be_empty),
            confirmText = stringResource(id = R.string.ok),
            onDismiss = { showAlertDialog = false }
        )
    }

    // Allow deeplinking into search screen with a query and type.
    // This will allow us to record searches in History.
    LaunchedEffect(key1 = searchQuery, key2 = searchOption) {
        if (searchQuery == null || searchOption == null) return@LaunchedEffect
        text = searchQuery
        selectedOption = searchOption
        viewModel.updateViewModelState(selectedOption, text)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RectangleShape,
                value = text,
                label = { Text(stringResource(id = R.string.search)) },
                placeholder = { Text(stringResource(id = R.string.search)) },
                maxLines = 1, // TODO: Seems like this is currently broken
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        coroutineScope.launch {
                            if (text.isEmpty()) {
                                showAlertDialog = true
                            } else {
                                viewModel.updateViewModelState(selectedOption, text)
                                lazyListState.scrollToItem(0)
                                focusManager.clearFocus()
                            }
                        }
                    }
                ),
                trailingIcon = {
                    if (text.isEmpty()) return@TextField
                    IconButton(onClick = {
                        text = ""
                        focusRequester.requestFocus()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(id = R.string.clear_search))
                    }
                },
                onValueChange = { newText ->
                    if (!newText.contains("\n")) {
                        text = newText
                    }
                }
            )

            ExposedDropdownMenuBox(
                modifier = Modifier.weight(1f),
                options = searchableResources,
                selectedOption = selectedOption,
                onSelectOption = {
                    selectedOption = it
                }
            )
        }

        PagingLoadingAndErrorHandler(
            lazyPagingItems = lazyPagingItems,
            lazyListState = lazyListState,
            snackbarHostState = snackbarHostState,
            noResultsText = stringResource(id = R.string.no_results_found_search)
        ) { listItemModel: ListItemModel? ->
            when (listItemModel) {
                is ArtistListItemModel -> {
                    ArtistListItem(artist = listItemModel) {
                        onItemClick(MusicBrainzResource.ARTIST, id, null)
                    }
                }
                is ReleaseGroupListItemModel -> {
                    // TODO: should see album type rather than year
                    ReleaseGroupListItem(releaseGroup = listItemModel) {
                        onItemClick(MusicBrainzResource.RELEASE_GROUP, id, getNameWithDisambiguation())
                    }
                }
                is ReleaseListItemModel -> {
                    ReleaseListItem(release = listItemModel) {
                        onItemClick(MusicBrainzResource.RELEASE, id, getNameWithDisambiguation())
                    }
                }
                is RecordingListItemModel -> {
                    RecordingListItem(recording = listItemModel) {
                        onItemClick(MusicBrainzResource.RECORDING, id, getNameWithDisambiguation())
                    }
                }
                is WorkListItemModel -> {
                    WorkListItem(work = listItemModel) {
                        onItemClick(MusicBrainzResource.WORK, id, getNameWithDisambiguation())
                    }
                }
                is AreaListItemModel -> {
                    AreaListItem(area = listItemModel) {
                        onItemClick(MusicBrainzResource.AREA, id, getNameWithDisambiguation())
                    }
                }
                is PlaceListItemModel -> {
                    PlaceListItem(place = listItemModel) {
                        onItemClick(MusicBrainzResource.PLACE, id, getNameWithDisambiguation())
                    }
                }
                is InstrumentListItemModel -> {
                    InstrumentListItem(instrument = listItemModel) {
                        onItemClick(MusicBrainzResource.INSTRUMENT, id, getNameWithDisambiguation())
                    }
                }
                is LabelListItemModel -> {
                    LabelListItem(label = listItemModel) {
                        onItemClick(MusicBrainzResource.LABEL, id, getNameWithDisambiguation())
                    }
                }
                is EventListItemModel -> {
                    EventListItem(event = listItemModel) {
                        onItemClick(MusicBrainzResource.EVENT, id, getNameWithDisambiguation())
                    }
                }
                is SeriesListItemModel -> {
                    SeriesListItem(series = listItemModel) {
                        onItemClick(MusicBrainzResource.SERIES, id, getNameWithDisambiguation())
                    }
                }
                is EndOfList -> {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        text = "End of search results."
                    )
                }
                else -> {
                    // Null. Do nothing.
                }
            }
        }
    }
}
