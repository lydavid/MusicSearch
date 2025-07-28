package ly.david.musicsearch.shared.domain.details

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.shared.domain.work.Work
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel

data class WorkDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val languages: List<String> = listOf(),
    override val iswcs: List<String> = listOf(),
    override val lastUpdated: Instant = Clock.System.now(),
    val attributes: List<WorkAttributeUiModel> = listOf(),
    override val artistCredits: List<ArtistCreditUiModel> = listOf(),
    override val imageMetadata: ImageMetadata = ImageMetadata(),
    override val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    override val urls: List<RelationListItemModel> = listOf(),
    override val aliases: List<BasicAlias> = listOf(),
) : Work, MusicBrainzDetailsModel {
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

    override fun withAliases(aliases: List<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}
