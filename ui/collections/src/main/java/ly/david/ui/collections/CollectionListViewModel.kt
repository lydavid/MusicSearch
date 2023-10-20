package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ly.david.musicsearch.data.musicbrainz.auth.store.MusicBrainzAuthStore
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.domain.collection.usecase.GetAllCollections
import org.koin.android.annotation.KoinViewModel

// TODO: remove ref to MusicBrainzAuthStore
//  logic should go through usecase
@KoinViewModel
class CollectionListViewModel(
    val appPreferences: AppPreferences,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val getAllCollections: GetAllCollections,
) : ViewModel() {

    data class ViewModelState(
        val username: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
        val showLocal: Boolean,
        val showRemote: Boolean,
    )

    private val username: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val showLocal: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val showRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val paramState = combine(
        username,
        query,
        isRemote,
        showLocal,
        showRemote,
    ) { entityId, query, isRemote, showLocal, showRemote ->
        ViewModelState(
            entityId,
            query,
            isRemote,
            showLocal,
            showRemote,
        )
    }.distinctUntilChanged()

    @OptIn(
        ExperimentalCoroutinesApi::class,
    )
    val pagedEntities: Flow<PagingData<CollectionListItemModel>> by lazy {
        paramState.flatMapLatest { state ->
            getAllCollections(
                username = state.username,
                showLocal = state.showLocal,
                showRemote = state.showRemote,
                query = state.query,
            )
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
    }

    fun getUsernameThenLoadCollections() {
        viewModelScope.launch {
            // Hack: We're using username in place of a UUID
            musicBrainzAuthStore.username.collectLatest {
                username.value = it
            }
        }
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun setShowLocal(show: Boolean) {
        appPreferences.setShowLocalCollections(show)
        showLocal.value = show
    }

    fun setShowRemote(show: Boolean) {
        appPreferences.setShowRemoteCollections(show)
        showRemote.value = show
    }
}
