package ly.david.data.repository

import androidx.paging.PagingSource
import ly.david.data.persistence.release.ReleaseWithCreditsAndCountries

interface ReleasesListRepository {
    suspend fun browseReleasesAndStore(resourceId: String, nextOffset: Int): Int
    suspend fun getRemoteReleasesCountByResource(resourceId: String): Int?
    suspend fun getLocalReleasesCountByResource(resourceId: String): Int
    suspend fun deleteReleasesByResource(resourceId: String)
    fun getReleasesPagingSource(resourceId: String, query: String): PagingSource<Int, ReleaseWithCreditsAndCountries>
}
