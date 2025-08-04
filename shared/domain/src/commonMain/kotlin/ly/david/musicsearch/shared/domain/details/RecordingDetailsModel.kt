package ly.david.musicsearch.shared.domain.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.recording.Recording
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

data class RecordingDetailsModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String? = null,
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val isrcs: List<String>? = null,
    override val lastUpdated: Instant = Clock.System.now(),
    override val artistCredits: List<ArtistCreditUiModel> = listOf(),
    override val imageMetadata: ImageMetadata = ImageMetadata(),
    override val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    override val urls: ImmutableList<RelationListItemModel> = persistentListOf(),
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : Recording, MusicBrainzDetailsModel {
    override fun withArtistCredits(artistCredits: List<ArtistCreditUiModel>): MusicBrainzDetailsModel {
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
