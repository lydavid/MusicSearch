package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.release.CoverArtArchiveUiModel
import ly.david.musicsearch.core.models.release.Release
import ly.david.musicsearch.core.models.release.TextRepresentationUiModel

data class ReleaseListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val date: String? = null,
    override val barcode: String? = null,
    override val status: String? = null,
    override val statusId: String? = null,
    override val countryCode: String? = null,
    override val packaging: String? = null,
    override val packagingId: String? = null,
    override val asin: String? = null,
    override val quality: String? = null,

    val coverArtArchive: CoverArtArchiveUiModel = CoverArtArchiveUiModel(),
    val textRepresentation: TextRepresentationUiModel? = null,
    val imageUrl: String? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,
    val formattedArtistCredits: String? = null,

    val releaseCountryCount: Int = 0,
) : ListItemModel(), Release
