package ly.david.musicsearch.data.repository.internal.paging

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.musicsearch.data.musicbrainz.DELAY_PAGED_API_CALLS_MS
import ly.david.musicsearch.data.musicbrainz.STARTING_OFFSET
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzModel
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

/**
 * This is not a [RemoteMediator] compared to [BrowseEntityRemoteMediator] and [LookupEntityRemoteMediator].
 *
 * We are not storing search results locally.
 * We want all search results to always be fresh.
 */
internal class SearchMusicBrainzPagingSource(
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
                entity = entity,
                queryString = queryString,
                currentOffset = currentOffset,
                limit = limit,
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
                nextKey = nextOffset,
            )
        } catch (ex: HandledException) {
            LoadResult.Error(ex)
        }
    }

    private data class QueryResults(
        val offset: Int,
        val data: List<MusicBrainzModel>,
    )

    private suspend fun getQueryResults(
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
                    limit = limit,
                )
                QueryResults(
                    queryArtists.offset,
                    queryArtists.artists,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                val queryReleaseGroups = searchApi.queryReleaseGroups(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryReleaseGroups.offset,
                    queryReleaseGroups.releaseGroups,
                )
            }

            MusicBrainzEntity.RELEASE -> {
                val queryReleases = searchApi.queryReleases(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryReleases.offset,
                    queryReleases.releases,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                val queryRecordings = searchApi.queryRecordings(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryRecordings.offset,
                    queryRecordings.recordings,
                )
            }

            MusicBrainzEntity.WORK -> {
                val queryWorks = searchApi.queryWorks(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryWorks.offset,
                    queryWorks.works,
                )
            }

            MusicBrainzEntity.AREA -> {
                val queryAreas = searchApi.queryAreas(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryAreas.offset,
                    queryAreas.areas,
                )
            }

            MusicBrainzEntity.PLACE -> {
                val queryPlaces = searchApi.queryPlaces(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryPlaces.offset,
                    queryPlaces.places,
                )
            }

            MusicBrainzEntity.INSTRUMENT -> {
                val queryInstruments = searchApi.queryInstruments(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryInstruments.offset,
                    queryInstruments.instruments,
                )
            }

            MusicBrainzEntity.LABEL -> {
                val queryLabels = searchApi.queryLabels(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryLabels.offset,
                    queryLabels.labels,
                )
            }

            MusicBrainzEntity.EVENT -> {
                val queryEvents = searchApi.queryEvents(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryEvents.offset,
                    queryEvents.events,
                )
            }

            MusicBrainzEntity.SERIES -> {
                val queryEvents = searchApi.querySeries(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryEvents.offset,
                    queryEvents.series,
                )
            }

            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.GENRE,
            MusicBrainzEntity.URL,
            -> {
                error(IllegalStateException("Cannot search $entity"))
            }
        }
    }
}
