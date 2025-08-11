package ly.david.musicsearch.shared.domain.details

import kotlinx.collections.immutable.ImmutableList
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.MusicBrainzModel
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

sealed interface MusicBrainzDetailsModel : MusicBrainzModel, NameWithDisambiguationAndAliases {
    val lastUpdated: Instant
    val artistCredits: List<ArtistCreditUiModel>
    val imageMetadata: ImageMetadata
    val wikipediaExtract: WikipediaExtract
    val urls: ImmutableList<RelationListItemModel>
    override val aliases: ImmutableList<BasicAlias>

    fun withArtistCredits(artistCredits: List<ArtistCreditUiModel>): MusicBrainzDetailsModel
    fun withImageMetadata(imageMetadata: ImageMetadata): MusicBrainzDetailsModel
    fun withWikipediaExtract(wikipediaExtract: WikipediaExtract): MusicBrainzDetailsModel
    fun withUrls(urls: ImmutableList<RelationListItemModel>): MusicBrainzDetailsModel
    override fun withAliases(aliases: ImmutableList<BasicAlias>): MusicBrainzDetailsModel
}
