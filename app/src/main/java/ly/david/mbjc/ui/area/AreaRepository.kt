package ly.david.mbjc.ui.area

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.domain.AreaUiModel
import ly.david.mbjc.data.domain.toAreaUiModel
import ly.david.mbjc.data.network.Lookup.Companion.INC_ALL_RELATIONS
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.ReleaseCountry
import ly.david.mbjc.data.persistence.area.ReleasesCountriesDao
import ly.david.mbjc.data.persistence.area.toAreaRoomModel
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel
import ly.david.mbjc.data.persistence.release.toReleaseRoomModel

@Singleton
internal class AreaRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    private val relationDao: RelationDao,
    private val releasesCountriesDao: ReleasesCountriesDao,
    private val releaseDao: ReleaseDao,
) {

    /**
     * Returns area for display.
     *
     * Looks up area, and stores all relevant data.
     *
     * This makes the assumption that after the first call, we have stored
     * all relationships as well.
     */
    suspend fun lookupArea(areaId: String, forceRefresh: Boolean = false): AreaUiModel {
        val areaRoomModel = areaDao.getArea(areaId)
        if (!forceRefresh && areaRoomModel != null) {
            return areaRoomModel.toAreaUiModel()
        }

        val areaMusicBrainzModel = musicBrainzApiService.lookupArea(
            areaId = areaId,
            include = INC_ALL_RELATIONS
        )
        val relations = mutableListOf<RelationRoomModel>()
        areaMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                resourceId = areaId,
                order = index
            )?.let { relationRoomModel ->
                relations.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relations)
        areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
        return areaMusicBrainzModel.toAreaUiModel()
    }

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
                    countryId = areaId
                )
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
