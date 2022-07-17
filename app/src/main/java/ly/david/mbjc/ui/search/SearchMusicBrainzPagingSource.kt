package ly.david.mbjc.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.IOException
import kotlinx.coroutines.delay
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.domain.toUiModel
import ly.david.mbjc.data.network.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzModel
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.STARTING_OFFSET
import retrofit2.HttpException

internal class SearchMusicBrainzPagingSource(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val resource: MusicBrainzResource,
    private val queryString: String,
) : PagingSource<Int, UiModel>() {

    override fun getRefreshKey(state: PagingState<Int, UiModel>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiModel> {
        val currentOffset = params.key ?: STARTING_OFFSET
        if (currentOffset != STARTING_OFFSET) {
            delay(DELAY_PAGED_API_CALLS_MS)
        }

        val limit = params.loadSize
        return try {
            val response = getQueryResults(
                musicBrainzApiService = musicBrainzApiService,
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
                data = searchResults.map { it.toUiModel() },
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
        musicBrainzApiService: MusicBrainzApiService,
        resource: MusicBrainzResource,
        queryString: String,
        currentOffset: Int,
        limit: Int
    ): QueryResults {
        return when (resource) {
            MusicBrainzResource.RELEASE_GROUP -> {
                val queryReleaseGroups = musicBrainzApiService.queryReleaseGroups(
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
                val queryReleases = musicBrainzApiService.queryReleases(
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
                val queryRecordings = musicBrainzApiService.queryRecordings(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryRecordings.offset,
                    queryRecordings.recordings
                )
            }
            MusicBrainzResource.AREA -> {
                val queryAreas = musicBrainzApiService.queryAreas(
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
                val queryPlaces = musicBrainzApiService.queryPlaces(
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
                val queryInstruments = musicBrainzApiService.queryInstruments(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryInstruments.offset,
                    queryInstruments.instruments
                )
            }
            // TODO: support rest
            else -> {
                // Artist; until we implement them all
                val queryArtists = musicBrainzApiService.queryArtists(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit
                )
                QueryResults(
                    queryArtists.offset,
                    queryArtists.artists
                )
            }
        }
    }
}
