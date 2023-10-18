package ly.david.mbjc.ui.work.recordings

import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.recording.usecase.GetRecordingsByEntity
import ly.david.ui.common.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class RecordingsByWorkViewModel(
    getRecordingsByEntity: GetRecordingsByEntity,
) : EntitiesByEntityViewModel<RecordingListItemModel>(
    entity = MusicBrainzEntity.WORK,
    getEntitiesByEntity = getRecordingsByEntity,
)
