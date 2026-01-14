package ly.david.musicsearch.shared.domain.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.shared.domain.work.Work
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import kotlin.time.Clock
import kotlin.time.Instant

data class WorkDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val type: String = "",
    override val languages: ImmutableList<String> = persistentListOf(),
    override val iswcs: ImmutableList<String> = persistentListOf(),
    override val lastUpdated: Instant = Clock.System.now(),
    val attributes: ImmutableList<WorkAttributeUiModel> = persistentListOf(),
    override val artistCredits: ImmutableList<ArtistCreditUiModel> = persistentListOf(),
    override val imageMetadata: ImageMetadata = ImageMetadata(),
    override val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    override val urls: ImmutableList<RelationListItemModel> = persistentListOf(),
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
    val listenCount: Long? = null,
) : Work, MusicBrainzDetailsModel {
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
