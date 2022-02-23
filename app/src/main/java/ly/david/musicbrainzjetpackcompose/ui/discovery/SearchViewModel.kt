package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.preferences.DELAY_PAGED_API_CALLS_MS
import ly.david.musicbrainzjetpackcompose.preferences.INITIAL_SEARCH_LIMIT
import ly.david.musicbrainzjetpackcompose.preferences.SEARCH_LIMIT
import ly.david.musicbrainzjetpackcompose.preferences.STARTING_OFFSET
import retrofit2.HttpException

/**
 * [Based off of article](https://medium.com/nerd-for-tech/pagination-in-android-with-paging-3-retrofit-and-kotlin-flow-2c2454ff776e)
 */
class ArtistsPagingSource(
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
            Log.d("Remove This", "load: currentOffset=$currentOffset")
            Log.d("Remove This", "load: artists.size=${artists.size}")
            Log.d("Remove This", "load: first=${artists.firstOrNull()?.name.orEmpty()}")
            Log.d("Remove This", "load: params.loadSize=${params.loadSize}")
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

internal class SearchViewModel : ViewModel() {

    val query: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val artists: Flow<PagingData<Artist>> =
        query.filterNot { it.isEmpty() }
            .flatMapLatest { query ->
                Pager(
                    config = PagingConfig(
                        pageSize = SEARCH_LIMIT,
                        initialLoadSize = INITIAL_SEARCH_LIMIT
                    ),
                    pagingSourceFactory = {
                        ArtistsPagingSource(queryString = query)
                    }
                ).flow
            }.cachedIn(viewModelScope)
}
