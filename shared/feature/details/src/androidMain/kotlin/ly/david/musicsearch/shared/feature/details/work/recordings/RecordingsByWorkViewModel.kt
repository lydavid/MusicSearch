package ly.david.musicsearch.shared.feature.details.work.recordings

import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.recording.usecase.GetRecordingsByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class RecordingsByWorkViewModel(
    getRecordingsByEntity: GetRecordingsByEntity,
) : EntitiesByEntityViewModel<RecordingListItemModel>(
    entity = MusicBrainzEntity.WORK,
    getEntitiesByEntity = getRecordingsByEntity,
)
