package ly.david.mbjc.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.GenreMusicBrainzModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.history.LookupHistoryDao
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.data.room.history.RecordLookupHistory
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class GenreScaffoldViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), MusicBrainzEntityViewModel, RecordLookupHistory {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.GENRE
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val genre: MutableStateFlow<GenreMusicBrainzModel?> = MutableStateFlow(null)

    fun onSelectedTabChange(
        genreId: String
    ) {
        viewModelScope.launch {
            try {
                val eventListItemModel = musicBrainzApiService.lookupGenre(genreId)
                if (title.value.isEmpty()) {
                    title.value = eventListItemModel.getNameWithDisambiguation()
                }
                genre.value = eventListItemModel
                isError.value = false
            } catch (ex: HttpException) {
                Timber.e(ex)
                isError.value = true
            } catch (ex: IOException) {
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
