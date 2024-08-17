package ly.david.musicsearch.shared.domain.recording.usecase

import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.recording.RecordingsByEntityRepository

class GetRecordingsByEntity(
    private val recordingsByEntityRepository: RecordingsByEntityRepository,
) : GetEntitiesByEntity<RecordingListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = recordingsByEntityRepository.observeRecordingsByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
