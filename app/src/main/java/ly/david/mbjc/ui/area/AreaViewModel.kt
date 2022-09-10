package ly.david.mbjc.ui.area

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.toAreaRoomModel
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao

@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    private val lookupHistoryDao: LookupHistoryDao
) : ViewModel() {

    suspend fun getArea(areaId: String): Area {
        // Use cached model.
        val areaRoomModel = areaDao.getArea(areaId)
        if (areaRoomModel != null) {
            recordLookupHistory(areaRoomModel)
            return areaRoomModel
        }

        val areaMusicBrainzModel = musicBrainzApiService.lookupArea(areaId)
        areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
        recordLookupHistory(areaMusicBrainzModel)
        return areaMusicBrainzModel
    }

    suspend fun recordLookupHistory(area: Area) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = area.name,
                resource = MusicBrainzResource.AREA,
                mbid = area.id
            )
        )
    }
}
