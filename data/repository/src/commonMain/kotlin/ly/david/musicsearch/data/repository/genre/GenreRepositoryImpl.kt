package ly.david.musicsearch.data.repository.genre

import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.genre.GenreRepository
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel

class GenreRepositoryImpl(
    private val lookupApi: LookupApi,
) : GenreRepository {

    override suspend fun lookupGenre(
        id: String,
        forceRefresh: Boolean,
    ): GenreListItemModel {
        return lookupApi.lookupGenre(genreId = id).toListItem()
    }
}

internal fun GenreMusicBrainzNetworkModel.toListItem() = GenreListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
)
