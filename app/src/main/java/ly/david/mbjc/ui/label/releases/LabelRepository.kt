package ly.david.mbjc.ui.label.releases

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Label
import ly.david.mbjc.data.domain.LabelUiModel
import ly.david.mbjc.data.domain.toLabelUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.label.toLabelRoomModel

@Singleton
internal class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var label: LabelUiModel? = null

    suspend fun lookupLabel(labelId: String): Label =
        label ?: run {

            val roomLabel = labelDao.getLabel(labelId)
            if (roomLabel != null) {
                incrementOrInsertLookupHistory(roomLabel)
                return roomLabel
            }

            val musicBrainzLabel = musicBrainzApiService.lookupLabel(labelId)

            labelDao.insert(musicBrainzLabel.toLabelRoomModel())

            incrementOrInsertLookupHistory(musicBrainzLabel)

            musicBrainzLabel.toLabelUiModel()
        }

    private suspend fun incrementOrInsertLookupHistory(label: Label) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = label.name,
                resource = MusicBrainzResource.LABEL,
                mbid = label.id
            )
        )
    }
}
