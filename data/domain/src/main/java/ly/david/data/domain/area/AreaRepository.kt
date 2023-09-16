package ly.david.data.domain.area

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi.Companion.INC_ALL_RELATIONS_EXCEPT_URLS
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.area.AreaDao
import ly.david.data.room.area.getAreaCountryCodes
import ly.david.data.room.area.toAreaRoomModel
import org.koin.core.annotation.Single

@Single
class AreaRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    /**
     * Returns area for display.
     *
     * Lookup area, and stores all relevant data.
     */
    suspend fun lookupArea(areaId: String): AreaScaffoldModel {
        val areaWithAllData = areaDao.getArea(areaId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(areaId)
        if (areaWithAllData != null && hasUrlsBeenSavedForEntity) {
            return areaWithAllData.toAreaScaffoldModel()
        }

        val areaMusicBrainzModel = musicBrainzApi.lookupArea(areaId)
        insertAllModels(areaMusicBrainzModel)
        return lookupArea(areaId)
    }

    private suspend fun insertAllModels(area: AreaMusicBrainzModel) {
        areaDao.withTransaction {
            areaDao.insert(area.toAreaRoomModel())
            areaDao.insertAllCountryCodes(area.getAreaCountryCodes())

            relationRepository.insertAllUrlRelations(
                entityId = area.id,
                relationMusicBrainzModels = area.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupArea(
            areaId = entityId,
            include = INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
