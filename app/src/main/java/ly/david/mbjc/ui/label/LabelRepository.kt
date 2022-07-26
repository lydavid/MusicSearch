package ly.david.mbjc.ui.label

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Label
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.data.persistence.RelationRoomModel
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.toLabelRoomModel
import ly.david.mbjc.data.persistence.toRelationRoomModel

@Singleton
internal class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var label: Label? = null

    suspend fun lookupLabel(labelId: String): Label =
        label ?: run {

            // Use cached model.
            val labelRoomModel = labelDao.getLabel(labelId)
            if (labelRoomModel != null) {
                incrementOrInsertLookupHistory(labelRoomModel)
                return labelRoomModel
            }

            val labelMusicBrainzModel = musicBrainzApiService.lookupLabel(labelId)
            labelDao.insert(labelMusicBrainzModel.toLabelRoomModel())

            val relations = mutableListOf<RelationRoomModel>()
            labelMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = labelId,
                    order = index
                )?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(relations)

            incrementOrInsertLookupHistory(labelMusicBrainzModel)
            labelMusicBrainzModel
        }

    private suspend fun incrementOrInsertLookupHistory(label: Label) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = label.name,
                resource = MusicBrainzResource.LABEL,
                mbid = label.id
            )
        )
    }
}
