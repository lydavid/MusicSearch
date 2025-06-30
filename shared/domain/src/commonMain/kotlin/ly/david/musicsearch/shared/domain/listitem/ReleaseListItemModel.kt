package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.Release
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel

data class ReleaseListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val date: String = "",
    override val barcode: String? = null,
    override val status: String? = null,
    override val statusId: String? = null,
    override val countryCode: String? = null,
    override val packaging: String? = null,
    override val packagingId: String? = null,
    override val asin: String? = null,
    override val quality: String? = null,

    val catalogNumbers: String? = null,

    val coverArtArchive: CoverArtArchiveUiModel = CoverArtArchiveUiModel(),
    val textRepresentation: TextRepresentationUiModel? = TextRepresentationUiModel(),
    val imageUrl: String? = null,
    val imageId: ImageId? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,
    val formattedArtistCredits: String? = null,

    val releaseCountryCount: Int = 0,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
) : EntityListItemModel, Release
