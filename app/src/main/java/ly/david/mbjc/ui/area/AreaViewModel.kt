package ly.david.mbjc.ui.area

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.toAreaRoomModel
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.ui.common.history.RecordLookupHistory

@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    override val lookupHistoryDao: LookupHistoryDao
) : ViewModel(), RecordLookupHistory {

    suspend fun getArea(areaId: String): Area {
        val areaRoomModel = areaDao.getArea(areaId)
        if (areaRoomModel != null) {
            return areaRoomModel
        }

        val areaMusicBrainzModel = musicBrainzApiService.lookupArea(areaId)
        areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
        return areaMusicBrainzModel
    }
}
