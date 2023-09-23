package ly.david.data.core

/**
 * Defines common properties between domain, network and persistence model.
 */
interface Release : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String
    val date: String?
    val barcode: String?
    val asin: String?
    val quality: String?
    val countryCode: String?
    val status: String?
    val statusId: String?
    val packaging: String?
    val packagingId: String?
//    val coverArtArchive: CoverArtArchive
//    val textRepresentation: TextRepresentation?
//    val releaseEvents: List<ReleaseEvent>? = null,

//    val media: List<Medium>? = null,

//    val artistCredits: List<MusicBrainzArtistCredit>? = null,

//    val labelInfoList: List<LabelInfo>? = null,
}
