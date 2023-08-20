package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.CollectionListItemModel
import ly.david.data.domain.listitem.toCollectionListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.collection.CollectionDao
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionDao: CollectionDao,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(),
    RecordLookupHistory {

    private var recordedLookup = false

    suspend fun getCollection(collectionId: String): CollectionListItemModel? {
        val collection = collectionDao.getCollectionWithEntities(collectionId)?.toCollectionListItemModel()

        if (!recordedLookup && collection != null) {
            recordLookupHistory(
                entityId = collectionId,
                entity = MusicBrainzEntity.COLLECTION,
                summary = collection.name
            )
            recordedLookup = true
        }

        return collection
    }
}
