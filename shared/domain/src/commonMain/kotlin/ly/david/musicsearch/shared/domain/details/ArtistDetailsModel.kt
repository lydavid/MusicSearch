package ly.david.musicsearch.shared.domain.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.Artist
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import kotlin.time.Clock
import kotlin.time.Instant

data class ArtistDetailsModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String = "",
    override val type: String = "",
    override val gender: String = "",
    val ipis: ImmutableList<String> = persistentListOf(),
    val isnis: ImmutableList<String> = persistentListOf(),
    val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val areaListItemModel: AreaListItemModel? = null,
    override val lastUpdated: Instant = Clock.System.now(),
    override val artistCredits: ImmutableList<ArtistCreditUiModel> = persistentListOf(),
    override val imageMetadata: ImageMetadata = ImageMetadata(),
    override val urls: ImmutableList<RelationListItemModel> = persistentListOf(),
    override val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : Artist, MusicBrainzDetailsModel {
    override fun withArtistCredits(artistCredits: ImmutableList<ArtistCreditUiModel>): MusicBrainzDetailsModel {
        return copy(artistCredits = artistCredits)
    }

    override fun withImageMetadata(imageMetadata: ImageMetadata): MusicBrainzDetailsModel {
        return copy(imageMetadata = imageMetadata)
    }

    override fun withWikipediaExtract(wikipediaExtract: WikipediaExtract): MusicBrainzDetailsModel {
        return copy(wikipediaExtract = wikipediaExtract)
    }

    override fun withUrls(urls: ImmutableList<RelationListItemModel>): MusicBrainzDetailsModel {
        return copy(urls = urls)
    }

    override fun withAliases(aliases: ImmutableList<BasicAlias>): MusicBrainzDetailsModel {
        return copy(aliases = aliases)
    }
}
