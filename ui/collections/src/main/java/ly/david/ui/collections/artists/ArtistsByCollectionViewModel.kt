package ly.david.ui.collections.artists

import ly.david.musicsearch.core.models.listitem.ArtistListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.artist.usecase.GetArtistsByEntity
import ly.david.ui.common.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ArtistsByCollectionViewModel(
    getArtistsByEntity: GetArtistsByEntity,
) : EntitiesByEntityViewModel<ArtistListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getArtistsByEntity,
)
