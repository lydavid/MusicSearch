package ly.david.data.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.toListItemModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.network.MusicBrainzModel
import ly.david.data.core.network.RecoverableNetworkException
import ly.david.data.network.api.DELAY_PAGED_API_CALLS_MS
import ly.david.data.network.api.STARTING_OFFSET
import ly.david.data.network.api.SearchApi

/**
 * This is not a [RemoteMediator] compared to [BrowseEntityRemoteMediator] and [LookupEntityRemoteMediator].
 *
 * We are not storing search results locally.
 * We want all search results to always be fresh.
 */
class SearchMusicBrainzPagingSource(
    private val searchApi: SearchApi,
    private val entity: MusicBrainzEntity,
    private val queryString: String,
) : PagingSource<Int, ListItemModel>() {

    override fun getRefreshKey(state: PagingState<Int, ListItemModel>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListItemModel> {
        return try {
            val currentOffset = if (params is LoadParams.Refresh) {
                STARTING_OFFSET
            } else {
                delay(DELAY_PAGED_API_CALLS_MS)
                params.key ?: STARTING_OFFSET
            }

            val limit = params.loadSize
            val response = getQueryResults(
                searchApi = searchApi,
                entity = entity,
                queryString = queryString,
                currentOffset = currentOffset,
                limit = limit
            )
            val searchResults = response.data
            val nextOffset = if (searchResults.size < limit) {
                null
            } else {
                currentOffset + searchResults.size
            }

            LoadResult.Page(
                data = searchResults.map { it.toListItemModel() },
                prevKey = if (currentOffset == STARTING_OFFSET) null else currentOffset,
                nextKey = nextOffset
            )
        } catch (exception: RecoverableNetworkException) {
            LoadResult.Error(exception)
        }
    }

    private data class QueryResults(
        val offset: Int,
        val data: List<MusicBrainzModel>,
    )

    private suspend fun getQueryResults(
        searchApi: SearchApi,
        entity: MusicBrainzEntity,
        queryString: String,
        currentOffset: Int,
        limit: Int,
    ): QueryResults {
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                val queryArtists = searchApi.queryArtists(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryArtists.offset,
                    queryArtists.artists
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                val queryReleaseGroups = searchApi.queryReleaseGroups(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryReleaseGroups.offset,
                    queryReleaseGroups.releaseGroups
                )
            }

            MusicBrainzEntity.RELEASE -> {
                val queryReleases = searchApi.queryReleases(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryReleases.offset,
                    queryReleases.releases
                )
            }

            MusicBrainzEntity.RECORDING -> {
                val queryRecordings = searchApi.queryRecordings(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryRecordings.offset,
                    queryRecordings.recordings
                )
            }

            MusicBrainzEntity.WORK -> {
                val queryWorks = searchApi.queryWorks(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryWorks.offset,
                    queryWorks.works
                )
            }

            MusicBrainzEntity.AREA -> {
                val queryAreas = searchApi.queryAreas(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryAreas.offset,
                    queryAreas.areas
                )
            }

            MusicBrainzEntity.PLACE -> {
                val queryPlaces = searchApi.queryPlaces(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryPlaces.offset,
                    queryPlaces.places
                )
            }

            MusicBrainzEntity.INSTRUMENT -> {
                val queryInstruments = searchApi.queryInstruments(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryInstruments.offset,
                    queryInstruments.instruments
                )
            }

            MusicBrainzEntity.LABEL -> {
                val queryLabels = searchApi.queryLabels(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryLabels.offset,
                    queryLabels.labels
                )
            }

            MusicBrainzEntity.EVENT -> {
                val queryEvents = searchApi.queryEvents(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryEvents.offset,
                    queryEvents.events
                )
            }

            MusicBrainzEntity.SERIES -> {
                val queryEvents = searchApi.querySeries(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryEvents.offset,
                    queryEvents.series
                )
            }

            // TODO: The following are not searchable. Is there a better model to switch on?
            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.GENRE,
            MusicBrainzEntity.URL,
            -> {
                QueryResults(
                    offset = 0,
                    data = listOf()
                )
            }
        }
    }
}
