package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.ArtistRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class ArtistViewModel @Inject constructor(
    private val repository: ArtistRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.ARTIST

    val title = MutableStateFlow("")
    val isError = MutableStateFlow(false)
    val artist: MutableStateFlow<ArtistListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun setTitle(title: String?) {
        this.title.value = title ?: return
    }

    fun onSelectedTabChange(
        artistId: String,
        selectedTab: ArtistTab
    ) {
        when (selectedTab) {
            ArtistTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val artistListItemModel = repository.lookupArtist(artistId)
                        if (title.value.isEmpty()) {
                            title.value = artistListItemModel.getNameWithDisambiguation()
                        }
                        artist.value = artistListItemModel
                        isError.value = false
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = artistId,
                            resource = resource,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            ArtistTab.RELATIONSHIPS -> loadRelations(artistId)
            else -> {
                // Not handled here.
            }
        }
    }
}
