package ly.david.musicsearch.shared.domain.details

import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.MusicBrainzModel
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

sealed interface MusicBrainzDetailsModel : MusicBrainzModel {
    val lastUpdated: Instant
    val artistCredits: List<ArtistCreditUiModel>
    val imageMetadata: ImageMetadata
    val wikipediaExtract: WikipediaExtract
    val urls: List<RelationListItemModel>

    fun withArtistCredits(artistCredits: List<ArtistCreditUiModel>): MusicBrainzDetailsModel
    fun withImageMetadata(imageMetadata: ImageMetadata): MusicBrainzDetailsModel
    fun withWikipediaExtract(wikipediaExtract: WikipediaExtract): MusicBrainzDetailsModel
    fun withUrls(urls: List<RelationListItemModel>): MusicBrainzDetailsModel
}
