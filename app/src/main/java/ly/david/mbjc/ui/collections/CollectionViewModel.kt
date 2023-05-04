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
        // TODO: crashes when coming from history if we've refreshed list of collections but failed
        //  the history entry will still exist but the collection it's trying to go to has been deleted
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
