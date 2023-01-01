package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ArtistListItemModel
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

    override val resource: MusicBrainzResource = MusicBrainzResource.ARTIST

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    suspend fun lookupArtist(artistId: String): ArtistListItemModel =
        repository.lookupArtist(artistId)
}
