package ly.david.musicsearch.shared.feature.collections.labels

import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.label.usecase.GetLabelsByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class LabelsByCollectionViewModel(
    getLabelsByEntity: GetLabelsByEntity,
) : EntitiesByEntityViewModel<LabelListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getLabelsByEntity,
)