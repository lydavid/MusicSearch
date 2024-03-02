package ly.david.musicsearch.shared.feature.collections.areas

import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.area.usecase.GetAreasByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

// TODO: instead of creating a Presenter for each of these VM, make one
//  might even have just one Ui as well, and it shows paging content based on Entity it receives
//  only releases and release groups are special in that they need to potentially fetch an image
//  how do we account for them?
@KoinViewModel
internal class AreasByCollectionViewModel(
    getAreasByEntity: GetAreasByEntity,
) : EntitiesByEntityViewModel<AreaListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getAreasByEntity,
)
