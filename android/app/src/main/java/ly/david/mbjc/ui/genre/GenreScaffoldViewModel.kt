package ly.david.mbjc.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

// TODO: use repository
@KoinViewModel
internal class GenreScaffoldViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val incrementLookupHistory: IncrementLookupHistory,
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
                incrementLookupHistory(
                    LookupHistory(
                        mbid = genreId,
                        title = title.value,
                        entity = entity,
                    ),
                )
                recordedLookup = true
            }
        }
    }
}
