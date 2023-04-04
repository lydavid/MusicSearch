package ly.david.mbjc.ui.collections

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.ui.common.history.RecordLookupHistory

@HiltViewModel
internal class CollectionViewModel @Inject constructor(
    private val collectionDao: CollectionDao,
    override val lookupHistoryDao: LookupHistoryDao
) : ViewModel(), RecordLookupHistory {

    private var recordedLookup = false

    suspend fun getCollection(collectionId: String): CollectionListItemModel {
        val collection = collectionDao.getCollectionWithEntities(collectionId).toCollectionListItemModel()

        if (!recordedLookup) {
            recordLookupHistory(
                resourceId = collectionId,
                resource = MusicBrainzResource.COLLECTION,
                summary = collection.name
            )
            recordedLookup = true
        }

        return collection
    }
}
