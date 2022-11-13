package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ArtistUiModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.ArtistRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList

@HiltViewModel
internal class ArtistViewModel @Inject constructor(
    private val repository: ArtistRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList, RelationsList.Delegate {

    init {
        relationsList.scope = viewModelScope
        relationsList.delegate = this
    }

    suspend fun lookupArtist(artistId: String): ArtistUiModel =
        repository.lookupArtist(artistId)

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? =
        repository.lookupArtistWithRelations(resourceId)
}
