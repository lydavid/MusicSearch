package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.CollectionListItemModel
import ly.david.data.domain.listitem.toCollectionListItemModel
import ly.david.data.room.collection.CollectionDao
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CollectionViewModel(
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
