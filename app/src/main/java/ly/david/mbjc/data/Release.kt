package ly.david.mbjc.data

/**
 * Defines common properties between domain, network and persistence model.
 */
internal interface Release : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String
    val date: String?
    val status: String?
    val barcode: String?
    val statusId: String?
    val countryCode: String?
    val packaging: String?
    val packagingId: String?
    val asin: String?
    val quality: String?
//    val coverArtArchive: CoverArtArchive = CoverArtArchive(),
//    val textRepresentation: TextRepresentation? = null,
//    val releaseEvents: List<ReleaseEvent>? = null,

//    val media: List<Medium>? = null,

//    val artistCredits: List<MusicBrainzArtistCredit>? = null,

//    val labelInfoList: List<LabelInfo>? = null,
}
