package ly.david.mbjc.ui.area

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.domain.AreaUiModel
import ly.david.mbjc.data.domain.toAreaUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.ReleaseCountry
import ly.david.mbjc.data.persistence.area.ReleasesCountriesDao
import ly.david.mbjc.data.persistence.area.toAreaRoomModel
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel
import ly.david.mbjc.data.persistence.release.toReleaseRoomModel

@Singleton
internal class AreaRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    private val releasesCountriesDao: ReleasesCountriesDao,
    private val releaseDao: ReleaseDao,
) {
    private var area: AreaUiModel? = null

    suspend fun lookupArea(areaId: String): Area =
        area ?: run {

            val areaRoomModel = areaDao.getArea(areaId)
            if (areaRoomModel != null) {
                return areaRoomModel
            }

            val areaMusicBrainzModel = musicBrainzApiService.lookupArea(areaId)
            areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
            areaMusicBrainzModel.toAreaUiModel()
        }.also { area = it }

    suspend fun browseReleasesAndStore(areaId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByArea(
            areaId = areaId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            areaDao.setReleaseCount(areaId, response.count)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel() })
        releasesCountriesDao.insertAll(
            musicBrainzReleases.map { release ->
                ReleaseCountry(
                    releaseId = release.id, 
                    countryId = areaId)
            }
        )

        return musicBrainzReleases.size
    }

    // Only difference between this and the stats one is this can return null
    suspend fun getTotalReleases(areaId: String): Int? =
        areaDao.getArea(areaId)?.releaseCount

    suspend fun getNumberOfReleasesByArea(areaId: String) =
        releasesCountriesDao.getNumberOfReleasesFromCountry(areaId)

    suspend fun deleteReleasesByArea(areaId: String) =
        releasesCountriesDao.deleteReleasesFromCountry(areaId)

    fun getReleasesPagingSource(areaId: String, query: String): PagingSource<Int, ReleaseRoomModel> = when {
        query.isEmpty() -> {
            releasesCountriesDao.getReleasesFromCountry(areaId)
        }
        else -> {
            releasesCountriesDao.getReleasesFromCountryFiltered(
                areaId = areaId,
                query = "%$query%"
            )
        }
    }
}
