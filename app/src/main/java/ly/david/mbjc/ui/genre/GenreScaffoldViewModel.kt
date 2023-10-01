package ly.david.mbjc.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.history.IncrementLookupHistoryUseCase
import ly.david.data.musicbrainz.GenreMusicBrainzModel
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.ui.common.MusicBrainzEntityViewModel
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class GenreScaffoldViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
) : ViewModel(),
    MusicBrainzEntityViewModel {

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
                incrementLookupHistoryUseCase(
                    LookupHistory(
                        mbid = genreId,
                        title = title.value,
                        entity = entity,
                    )
                )
                recordedLookup = true
            }
        }
    }
}
