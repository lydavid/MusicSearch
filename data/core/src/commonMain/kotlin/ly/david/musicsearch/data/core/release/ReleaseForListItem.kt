package ly.david.musicsearch.data.core.release

data class ReleaseForListItem(
    override val id: String,
    override val name: String,
    override val disambiguation: String,
    override val date: String?,
    override val barcode: String?,
    override val asin: String?,
    override val quality: String?,
    override val countryCode: String?,
    override val status: String?,
    override val statusId: String?,
    override val packaging: String?,
    override val packagingId: String?,
    val script: String?,
    val language: String?,
    val coverArtCount: Int,
    val formattedArtistCreditNames: String,
    val thumbnailUrl: String?,
    val releaseCountryCount: Int,
) : Release
