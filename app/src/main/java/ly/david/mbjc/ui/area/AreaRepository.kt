package ly.david.mbjc.ui.area

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.toAreaRoomModel
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel

@Singleton
internal class AreaRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var area: Area? = null

    suspend fun lookupArea(areaId: String): Area =
        area ?: run {

            // Use cached model.
            val areaRoomModel = areaDao.getArea(areaId)
            if (areaRoomModel != null) {
                incrementOrInsertLookupHistory(areaRoomModel)
                return areaRoomModel
            }

            val areaMusicBrainzModel = musicBrainzApiService.lookupArea(areaId)
            areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())

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

            incrementOrInsertLookupHistory(areaMusicBrainzModel)
            areaMusicBrainzModel
        }

    private suspend fun incrementOrInsertLookupHistory(area: Area) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = area.name,
                resource = MusicBrainzResource.AREA,
                mbid = area.id
            )
        )
    }
}
