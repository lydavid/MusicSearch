package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.AreaScaffoldModel
import ly.david.data.domain.toAreaScaffoldModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.INC_ALL_RELATIONS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.getAreaCountryCodes
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel

@Singleton
class AreaRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    private val relationDao: RelationDao,
) : RelationsListRepository {

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
    ): AreaScaffoldModel {
        val areaRoomModel = areaDao.getArea(areaId)
        if (!forceRefresh && areaRoomModel != null && hasRelationsBeenStored()) {
            return areaRoomModel.toAreaScaffoldModel()
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

        return areaMusicBrainzModel.toAreaScaffoldModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArea(
            areaId = resourceId,
            include = INC_ALL_RELATIONS
        ).relations
    }
}
