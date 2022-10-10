package ly.david.mbjc.ui.label

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Label
import ly.david.mbjc.data.domain.LabelUiModel
import ly.david.mbjc.data.domain.toLabelUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.label.toLabelRoomModel

@Singleton
internal class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
) {
    private var label: LabelUiModel? = null

    suspend fun lookupLabel(labelId: String): Label =
        label ?: run {

            val roomLabel = labelDao.getLabel(labelId)
            if (roomLabel != null) {
                return roomLabel
            }

            val musicBrainzLabel = musicBrainzApiService.lookupLabel(labelId)

            labelDao.insert(musicBrainzLabel.toLabelRoomModel())

            musicBrainzLabel.toLabelUiModel()
        }
}
