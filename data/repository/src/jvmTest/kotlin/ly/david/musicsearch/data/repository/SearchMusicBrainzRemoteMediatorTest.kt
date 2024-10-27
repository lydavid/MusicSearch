package ly.david.musicsearch.data.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.test.runTest
import ly.david.data.test.searchAreasResponse
import ly.david.data.test.searchArtistsResponse
import ly.david.data.test.searchEventsResponse
import ly.david.data.test.searchInstrumentsResponse
import ly.david.data.test.searchLabelsResponse
import ly.david.data.test.searchPlacesResponse
import ly.david.data.test.searchRecordingsResponse
import ly.david.data.test.searchReleaseGroupsResponse
import ly.david.data.test.searchReleasesResponse
import ly.david.data.test.searchSeriesResponse
import ly.david.data.test.searchWorksResponse
import ly.david.data.test.toFakeMusicBrainzModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.searchableEntities
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.musicbrainz.api.SearchAreasResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchArtistsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchEventsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchLabelsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchPlacesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchSeriesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchWorksResponse
import ly.david.musicsearch.data.repository.internal.paging.SearchMusicBrainzRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.toListItemModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class SearchMusicBrainzRemoteMediatorTest(private val entity: MusicBrainzEntity) {

    private class FakeSearchApi : SearchApi {
        // region Search
        override suspend fun queryAreas(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchAreasResponse {
            return searchAreasResponse
        }

        override suspend fun queryArtists(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchArtistsResponse {
            return searchArtistsResponse
        }

        override suspend fun queryEvents(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchEventsResponse {
            return searchEventsResponse
        }

        override suspend fun queryInstruments(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchInstrumentsResponse {
            return searchInstrumentsResponse
        }

        override suspend fun queryLabels(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchLabelsResponse {
            return searchLabelsResponse
        }

        override suspend fun queryPlaces(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchPlacesResponse {
            return searchPlacesResponse
        }

        override suspend fun queryRecordings(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchRecordingsResponse {
            return searchRecordingsResponse
        }

        override suspend fun queryReleases(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchReleasesResponse {
            return searchReleasesResponse
        }

        override suspend fun queryReleaseGroups(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchReleaseGroupsResponse {
            return searchReleaseGroupsResponse
        }

        override suspend fun querySeries(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchSeriesResponse {
            return searchSeriesResponse
        }

        override suspend fun queryWorks(
            query: String,
            limit: Int,
            offset: Int,
        ): SearchWorksResponse {
            return searchWorksResponse
        }
        // endregion
    }

    @Test
    fun loadEachEntity() = runTest {
        val pagingSource = SearchMusicBrainzRemoteMediator(
            FakeSearchApi(),
            entity,
            "",
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    entity.toFakeMusicBrainzModel().toListItemModel(),
                ),
                prevKey = null,
                nextKey = 1,
            ),
            pagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false,
                ),
            ),
        )

        // if we try to load another page, it will always spit out the same page since we've mocked our api service
        // also can't filter
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzEntity> {
            return searchableEntities
        }
    }
}
