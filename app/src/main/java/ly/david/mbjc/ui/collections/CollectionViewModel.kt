package ly.david.mbjc.ui.collections

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.persistence.collection.CollectionDao

@HiltViewModel
internal class CollectionViewModel @Inject constructor(
    private val collectionDao: CollectionDao
) : ViewModel() {

    suspend fun getCollection(collectionId: String): CollectionListItemModel =
        collectionDao.getCollectionWithEntities(collectionId).toCollectionListItemModel()
}
