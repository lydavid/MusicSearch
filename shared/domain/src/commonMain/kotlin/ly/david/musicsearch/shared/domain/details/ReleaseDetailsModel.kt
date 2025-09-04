package ly.david.musicsearch.shared.domain.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.release.Release
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import kotlin.time.Clock
import kotlin.time.Instant

data class ReleaseDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val date: String = "",
    override val barcode: String = "",
    val status: ReleaseStatus? = null,
    override val countryCode: String = "",
    override val packaging: String = "",
    override val packagingId: String = "",
    override val asin: String = "",
    override val quality: String = "",

    override val lastUpdated: Instant = Clock.System.now(),
    override val imageMetadata: ImageMetadata = ImageMetadata(),
    override val artistCredits: ImmutableList<ArtistCreditUiModel> = persistentListOf(),
    override val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    override val urls: ImmutableList<RelationListItemModel> = persistentListOf(),
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),

    val textRepresentation: TextRepresentationUiModel = TextRepresentationUiModel(),

    val formattedFormats: String = "",
    val formattedTracks: String = "",

    val releaseGroup: ReleaseGroupForRelease? = null,
    val areas: List<AreaListItemModel> = listOf(),
    val labels: List<LabelListItemModel> = listOf(),

    val releaseLength: Int? = null,
    val hasNullLength: Boolean = false,
) : Release, MusicBrainzDetailsModel {
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
