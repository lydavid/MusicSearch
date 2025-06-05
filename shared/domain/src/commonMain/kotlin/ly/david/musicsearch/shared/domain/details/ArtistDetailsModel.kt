package ly.david.musicsearch.shared.domain.details

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.Artist
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

data class ArtistDetailsModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    val ipis: List<String>? = null,
    val isnis: List<String>? = null,
    val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val areaListItemModel: AreaListItemModel? = null,
    override val lastUpdated: Instant = Clock.System.now(),
    override val artistCredits: List<ArtistCreditUiModel> = listOf(),
    override val imageMetadata: ImageMetadata = ImageMetadata(),
    override val urls: List<RelationListItemModel> = listOf(),
    override val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
) : Artist, MusicBrainzDetailsModel {
    override fun withArtistCredits(artistCredits: List<ArtistCreditUiModel>): MusicBrainzDetailsModel {
        return copy(artistCredits = artistCredits)
    }

    override fun withImageMetadata(imageMetadata: ImageMetadata): MusicBrainzDetailsModel {
        return copy(imageMetadata = imageMetadata)
    }

    override fun withWikipediaExtract(wikipediaExtract: WikipediaExtract): MusicBrainzDetailsModel {
        return copy(wikipediaExtract = wikipediaExtract)
    }

    override fun withUrls(urls: List<RelationListItemModel>): MusicBrainzDetailsModel {
        return copy(urls = urls)
    }
}
