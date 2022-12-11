package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.LabelListItemModel
import ly.david.data.domain.toLabelListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.toLabelRoomModel

@Singleton
class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
) : RelationsListRepository {

    suspend fun lookupLabel(labelId: String): LabelListItemModel {
        val labelRoomModel = labelDao.getLabel(labelId)
        if (labelRoomModel != null) {
            return labelRoomModel.toLabelListItemModel()
        }

        val labelMusicBrainzModel = musicBrainzApiService.lookupLabel(labelId)

        labelDao.insert(labelMusicBrainzModel.toLabelRoomModel())

        return labelMusicBrainzModel.toLabelListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupLabel(
            labelId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
