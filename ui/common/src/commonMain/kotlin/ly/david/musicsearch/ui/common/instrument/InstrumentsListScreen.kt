package ly.david.musicsearch.ui.common.instrument

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun InstrumentsListScreen(
    state: InstrumentsListUiState,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = state.lazyPagingItems,
        modifier = modifier,
        lazyListState = state.lazyListState,
    ) { listItemModel: InstrumentListItemModel? ->
        when (listItemModel) {
            is InstrumentListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        InstrumentListItem(
                            instrument = listItemModel,
                        ) {
                            onItemClick(
                                MusicBrainzEntity.INSTRUMENT,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
                    onDelete = {
                        onDeleteFromCollection?.invoke(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
