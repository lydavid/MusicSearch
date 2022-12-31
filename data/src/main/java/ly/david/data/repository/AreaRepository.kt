package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.AreaScaffoldModel
import ly.david.data.domain.toAreaScaffoldModel
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.INC_ALL_RELATIONS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.getAreaCountryCodes
import ly.david.data.persistence.area.toAreaRoomModel

@Singleton
class AreaRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao
) : RelationsListRepository {

    /**
     * Returns area for display.
     *
     * Looks up area, and stores all relevant data.
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

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArea(
            areaId = resourceId,
            include = INC_ALL_RELATIONS
        ).relations
    }
}
