package ly.david.mbjc.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.network.GenreMusicBrainzModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.network.RecoverableNetworkException
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import timber.log.Timber

@HiltViewModel
internal class GenreScaffoldViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.GENRE
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val genre: MutableStateFlow<GenreMusicBrainzModel?> = MutableStateFlow(null)

    fun onSelectedTabChange(genreId: String) {
        viewModelScope.launch {
            try {
                val eventListItemModel = musicBrainzApi.lookupGenre(genreId)
                if (title.value.isEmpty()) {
                    title.value = eventListItemModel.getNameWithDisambiguation()
                }
                genre.value = eventListItemModel
                isError.value = false
            } catch (ex: RecoverableNetworkException) {
                Timber.e(ex)
                isError.value = true
            }

            if (!recordedLookup) {
                recordLookupHistory(
                    entityId = genreId,
                    entity = entity,
                    summary = title.value
                )
                recordedLookup = true
            }
        }
    }
}
