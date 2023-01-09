package ly.david.mbjc.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.GenreMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory

@HiltViewModel
internal class GenreScaffoldViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.GENRE
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
            } catch (ex: Exception) {
                isError.value = true
            }

            if (!recordedLookup) {
                recordLookupHistory(
                    resourceId = genreId,
                    resource = resource,
                    summary = title.value
                )
                recordedLookup = true
            }
        }
    }
}
