package ly.david.musicsearch.shared.domain.series.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.appendLastUpdatedBanner
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.series.SeriesListRepository

interface GetSeries {
    operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>>
}

class GetSeriesImpl(
    private val seriesListRepository: SeriesListRepository,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<ListItemModel>, GetSeries {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            seriesListRepository.observeSeries(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .cachedIn(scope = coroutineScope)
                .appendLastUpdatedBanner(
                    browseRemoteMetadataRepository = browseRemoteMetadataRepository,
                    browseMethod = browseMethod,
                    browseEntity = MusicBrainzEntity.SERIES,
                )
                .distinctUntilChanged()
        }
    }
}
