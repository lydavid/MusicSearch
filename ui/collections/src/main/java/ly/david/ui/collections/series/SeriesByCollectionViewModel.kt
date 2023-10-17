package ly.david.ui.collections.series

import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.series.usecase.GetSeriesByEntity
import ly.david.ui.common.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SeriesByCollectionViewModel(
    getSeriesByEntity: GetSeriesByEntity,
) : EntitiesByEntityViewModel<SeriesListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getSeriesByEntity,
)
