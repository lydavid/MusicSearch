package ly.david.musicsearch.ui.common.collection

import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.error.Feedback
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addedMultipleToCollection
import musicsearch.ui.common.generated.resources.addedMultipleToCollectionSomeAlreadyAdded
import musicsearch.ui.common.generated.resources.addedOneToCollection
import musicsearch.ui.common.generated.resources.alreadyInCollection
import musicsearch.ui.common.generated.resources.collectionDoesNotExist
import musicsearch.ui.common.generated.resources.deletedCountFromCollection
import musicsearch.ui.common.generated.resources.deletingCountFromCollection
import musicsearch.ui.common.generated.resources.failedToAddToCollection
import musicsearch.ui.common.generated.resources.failedToDeleteFromCollection
import musicsearch.ui.common.generated.resources.syncingWithMusicBrainz
import org.jetbrains.compose.resources.getString

suspend fun Feedback<EditACollectionFeedback>.getMessage(): String {
    return when (val data = this.data) {
        is EditACollectionFeedback.SyncingWithMusicBrainz -> getString(Res.string.syncingWithMusicBrainz)

        is EditACollectionFeedback.Deleting -> {
            getString(Res.string.deletingCountFromCollection, data.count, data.collectionName)
        }

        is EditACollectionFeedback.Deleted -> {
            getString(Res.string.deletedCountFromCollection, data.count, data.collectionName)
        }

        is EditACollectionFeedback.FailedToDelete -> {
            getString(Res.string.failedToDeleteFromCollection, data.collectionName, data.errorMessage)
        }

        EditACollectionFeedback.DoesNotExist -> getString(Res.string.collectionDoesNotExist)

        is EditACollectionFeedback.FailedToAdd -> getString(
            Res.string.failedToAddToCollection,
            data.collectionName,
            data.errorMessage,
        )

        is EditACollectionFeedback.AlreadyIn -> getString(Res.string.alreadyInCollection, data.collectionName)

        is EditACollectionFeedback.AddedOne -> getString(Res.string.addedOneToCollection, data.collectionName)

        is EditACollectionFeedback.AddedMultiple -> getString(
            Res.string.addedMultipleToCollection,
            data.newInsertions,
            data.collectionName,
        )

        is EditACollectionFeedback.AddedMultipleSomeAlreadyAdded -> getString(
            Res.string.addedMultipleToCollectionSomeAlreadyAdded,
            data.newInsertions,
            data.collectionName,
            data.countAlreadyAdded,
        )
    }
}
