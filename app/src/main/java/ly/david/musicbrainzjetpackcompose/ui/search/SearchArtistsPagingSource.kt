package ly.david.musicbrainzjetpackcompose.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.IOException
import kotlinx.coroutines.delay
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.preferences.DELAY_PAGED_API_CALLS_MS
import ly.david.musicbrainzjetpackcompose.preferences.STARTING_OFFSET
import retrofit2.HttpException

class SearchArtistsPagingSource(
    val queryString: String
) : PagingSource<Int, Artist>() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        val currentOffset = params.key ?: STARTING_OFFSET
        if (currentOffset != STARTING_OFFSET) {
            delay(DELAY_PAGED_API_CALLS_MS)
        }
        return try {
            val response = musicBrainzApiService.queryArtists(
                query = queryString,
                offset = currentOffset,
                limit = params.loadSize
            )
            val artists = response.artists
            val nextOffset = if (artists.isEmpty()) {
                null
            } else {
                currentOffset + artists.size
            }
            LoadResult.Page(
                data = artists,
                prevKey = if (currentOffset == STARTING_OFFSET) null else currentOffset,
                nextKey = nextOffset
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
