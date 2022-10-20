package ly.david.mbjc.ui.place

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.repository.PlaceRepository
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class PlaceViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupPlace(placeId: String) = placeRepository.lookupPlace(placeId).also {
        loadRelations(it.id)
    }
}
