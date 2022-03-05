package ly.david.mbjc.ui.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.LookupHistoryDao

// TODO: this needs to access data stored in Room/SharedPreferences
//  need to store two piece of info for each screen: route, id
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val lookupHistoryDao: LookupHistoryDao
): ViewModel() {

    suspend fun getAllLookupHistory() = lookupHistoryDao.getAllLookupHistory()
}
