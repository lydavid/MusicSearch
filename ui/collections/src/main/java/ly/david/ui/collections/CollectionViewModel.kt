package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.domain.history.IncrementLookupHistoryUseCase
import ly.david.musicsearch.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.domain.listitem.toCollectionListItemModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CollectionViewModel(
    private val collectionDao: CollectionDao,
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
) : ViewModel() {

    private var recordedLookup = false

    fun getCollection(collectionId: String): CollectionListItemModel? {
        val collection = collectionDao.getCollection(collectionId)?.toCollectionListItemModel()

        if (!recordedLookup && collection != null) {
            incrementLookupHistoryUseCase(
                LookupHistory(
                    mbid = collectionId,
                    title = collection.name,
                    entity = MusicBrainzEntity.COLLECTION,
                )
            )
            recordedLookup = true
        }

        return collection
    }
}
