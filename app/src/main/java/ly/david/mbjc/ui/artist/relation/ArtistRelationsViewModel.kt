package ly.david.mbjc.ui.artist.relation

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class ArtistRelationsViewModel @Inject constructor(
    private val artistRelationsRepository: ArtistRelationsRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupArtistRelations(artistId: String): Artist = artistRelationsRepository.lookupArtistRelations(artistId).also {
        this.resourceId.value = it.id
    }
}
