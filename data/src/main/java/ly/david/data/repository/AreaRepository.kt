package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.AreaCardModel
import ly.david.data.domain.toCardModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RelationMusicBrainzModel
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
import ly.david.data.persistence.release.ReleaseWithReleaseCountries
import ly.david.data.persistence.release.toReleaseRoomModel

@Singleton
class AreaRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    private val relationDao: RelationDao,
    private val releasesCountriesDao: ReleasesCountriesDao,
    private val releaseDao: ReleaseDao,
) : ReleasesListRepository, RelationsListRepository {

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
    ): AreaCardModel {
        val areaRoomModel = areaDao.getArea(areaId)
        val countryCodes = areaDao.getCountryCodes(areaId)
        if (!forceRefresh && areaRoomModel != null && hasRelationsBeenStored()) {
            return areaRoomModel.toCardModel(countryCodes?.map { it.code })
        }

        val areaMusicBrainzModel = musicBrainzApiService.lookupArea(
            areaId = areaId,
            include = INC_ALL_RELATIONS
        )

        // TODO: repeated logic
        //  screens that starts on relationship makes us do this
        //  We do this to avoid double lookup call
        //  We could technically load relations first, then look for its title if not provided with it
        //  at which point, we should be using cached room model
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
        markResourceHasRelations()

        areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
        areaDao.insertAllCountryCodes(areaMusicBrainzModel.getAreaCountryCodes())

        return areaMusicBrainzModel.toCardModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArea(
            areaId = resourceId,
            include = INC_ALL_RELATIONS
        ).relations
    }

    override suspend fun browseReleasesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByArea(
            areaId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResource(
                browseResourceRoomModel = BrowseResourceOffset(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementOffsetForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel() })
        releasesCountriesDao.insertAll(
            musicBrainzReleases.map { release ->
                ReleaseCountry(
                    releaseId = release.id,
                    countryId = resourceId,
                    date = release.date
                )
            }
        )

        return musicBrainzReleases.size
    }

    override suspend fun getRemoteReleasesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalReleasesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteReleasesByResource(resourceId: String) {
        releasesCountriesDao.deleteReleasesFromCountry(resourceId)
        relationDao.deleteBrowseResourceOffsetByResource(resourceId, MusicBrainzResource.RELEASE)
    }

    override fun getReleasesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseWithReleaseCountries> = when {
        query.isEmpty() -> {
            releasesCountriesDao.getReleasesFromCountry(resourceId)
        }
        else -> {
            releasesCountriesDao.getReleasesFromCountryFiltered(
                areaId = resourceId,
                query = "%$query%"
            )
        }
    }
}
