package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.AreaUiModel
import ly.david.data.domain.toAreaUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.LookupApi.Companion.INC_ALL_RELATIONS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.area.getAreaCountryCodes
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.relation.BrowseResourceOffset
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseRoomModel
import ly.david.data.persistence.release.toReleaseRoomModel

@Singleton
class AreaRepository @Inject constructor(
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
    suspend fun lookupArea(
        areaId: String,
        forceRefresh: Boolean = false,
        hasRelationsBeenStored: suspend () -> Boolean,
        markResourceHasRelations: suspend () -> Unit
    ): AreaUiModel {
        val areaRoomModel = areaDao.getArea(areaId)
        val countryCodes = areaDao.getCountryCodes(areaId)
        if (!forceRefresh && areaRoomModel != null && hasRelationsBeenStored()) {
            return areaRoomModel.toAreaUiModel(countryCodes?.map { it.code })
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

        areaDao.insertAllCountryCodes(areaMusicBrainzModel.getAreaCountryCodes())

        markResourceHasRelations()

        return areaMusicBrainzModel.toAreaUiModel()
    }

    suspend fun browseReleasesAndStore(areaId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByArea(
            areaId = areaId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResource(
                browseResourceRoomModel = BrowseResourceOffset(
                    resourceId = areaId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementOffsetForResource(areaId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel() })
        releasesCountriesDao.insertAll(
            musicBrainzReleases.map { release ->
                ReleaseCountry(
                    releaseId = release.id,
                    countryId = areaId,
                    date = release.date
                )
            }
        )

        return musicBrainzReleases.size
    }

    suspend fun getRemoteReleasesByAreaCount(areaId: String): Int? =
        relationDao.getBrowseResourceOffset(areaId, MusicBrainzResource.RELEASE)?.remoteCount

    suspend fun getLocalReleasesByAreaCount(areaId: String) =
        relationDao.getBrowseResourceOffset(areaId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    suspend fun deleteReleasesByArea(areaId: String) {
        releasesCountriesDao.deleteReleasesFromCountry(areaId)
        relationDao.deleteBrowseResourceOffsetByResource(areaId, MusicBrainzResource.RELEASE)
    }

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
