package ly.david.musicsearch.shared.feature.collections.works

import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.work.usecase.GetWorksByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class WorksByCollectionViewModel(
    getWorksByEntity: GetWorksByEntity,
) : EntitiesByEntityViewModel<WorkListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getWorksByEntity,
)