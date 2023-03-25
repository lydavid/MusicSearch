package ly.david.mbjc.ui.collections

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.collection.CollectionDao

@HiltViewModel
internal class MusicBrainzCollectionViewModel @Inject constructor(
    private val collectionDao: CollectionDao
) : ViewModel() {

    suspend fun getCollection(collectionId: String) = collectionDao.getCollection(collectionId)
}
