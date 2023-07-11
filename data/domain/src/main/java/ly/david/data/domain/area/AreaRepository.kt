package ly.david.data.domain.area

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.INC_ALL_RELATIONS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.area.AreaDao
import ly.david.data.room.area.getAreaCountryCodes
import ly.david.data.room.area.toAreaRoomModel

@Singleton
class AreaRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
) : RelationsListRepository {

    /**
     * Returns area for display.
     *
     * Lookup area, and stores all relevant data.
     */
    suspend fun lookupArea(areaId: String): AreaScaffoldModel {
        val areaRoomModel = areaDao.getArea(areaId)
        if (areaRoomModel != null) {
            return areaRoomModel.toAreaScaffoldModel()
        }

        val areaMusicBrainzModel = musicBrainzApiService.lookupArea(areaId)
        insertAllModels(areaMusicBrainzModel)
        return areaMusicBrainzModel.toAreaScaffoldModel()
    }

    private suspend fun insertAllModels(area: AreaMusicBrainzModel) {
        areaDao.insert(area.toAreaRoomModel())
        areaDao.insertAllCountryCodes(area.getAreaCountryCodes())
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArea(
            areaId = entityId,
            include = INC_ALL_RELATIONS
        ).relations
    }
}
