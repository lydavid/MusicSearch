package ly.david.ui.collections.recordings

import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.recording.usecase.GetRecordingsByEntity
import ly.david.ui.common.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class RecordingsByCollectionViewModel(
    getRecordingsByEntity: GetRecordingsByEntity,
) : EntitiesByEntityViewModel<RecordingListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getRecordingsByEntity,
)
