package ly.david.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import java.io.IOException
import kotlinx.coroutines.delay
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.toListItemModel
import ly.david.data.network.MusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.DELAY_PAGED_API_CALLS_MS
import ly.david.data.network.api.STARTING_OFFSET
import ly.david.data.network.api.SearchApi
import retrofit2.HttpException

/**
 * This is not a [RemoteMediator] compared to [BrowseResourceRemoteMediator] and [LookupResourceRemoteMediator].
 * This is because we are not storing search results locally.
 * We want all search results to be fresh.
 */
class SearchMusicBrainzPagingSource(
    private val searchApi: SearchApi,
    private val resource: MusicBrainzResource,
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
                resource = resource,
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
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    private data class QueryResults(
        val offset: Int,
        val data: List<MusicBrainzModel>
    )

    private suspend fun getQueryResults(
        searchApi: SearchApi,
        resource: MusicBrainzResource,
        queryString: String,
        currentOffset: Int,
        limit: Int
    ): QueryResults {
        return when (resource) {
            MusicBrainzResource.ARTIST -> {
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
            MusicBrainzResource.RELEASE_GROUP -> {
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
            MusicBrainzResource.RELEASE -> {
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
            MusicBrainzResource.RECORDING -> {
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
            MusicBrainzResource.WORK -> {
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
            MusicBrainzResource.AREA -> {
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
            MusicBrainzResource.PLACE -> {
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
            MusicBrainzResource.INSTRUMENT -> {
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
            MusicBrainzResource.LABEL -> {
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
            MusicBrainzResource.EVENT -> {
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
            MusicBrainzResource.SERIES -> {
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

            // TODO: The following are not queryable. Is there a better model to switch on?
            MusicBrainzResource.GENRE -> TODO()
            MusicBrainzResource.URL -> TODO()
        }
    }
}
