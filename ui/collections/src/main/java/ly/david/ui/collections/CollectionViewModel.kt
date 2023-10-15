package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CollectionViewModel(
    private val collectionDao: CollectionDao,
    private val incrementLookupHistory: IncrementLookupHistory,
) : ViewModel() {

    private var recordedLookup = false

    fun getCollection(collectionId: String): CollectionListItemModel {
        val collection = collectionDao.getCollection(collectionId)

        if (!recordedLookup) {
            incrementLookupHistory(
                LookupHistory(
                    mbid = collectionId,
                    title = collection.name,
                    entity = MusicBrainzEntity.COLLECTION,
                ),
            )
            recordedLookup = true
        }

        return collection
    }
}
