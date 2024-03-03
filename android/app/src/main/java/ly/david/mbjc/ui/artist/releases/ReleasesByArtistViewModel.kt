package ly.david.mbjc.ui.artist.releases

import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByArtistViewModel(
    getReleasesByEntity: GetReleasesByEntity,
) : EntitiesByEntityViewModel<ReleaseListItemModel>(
    entity = MusicBrainzEntity.ARTIST,
    getEntitiesByEntity = getReleasesByEntity,
)