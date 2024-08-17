package ly.david.musicsearch.shared.domain.artist

interface ArtistCreditName {
    val name: String
    val joinPhrase: String?
}

/**
 * Get all artists' name in a credit, joined by their [ArtistCreditName.joinPhrase].
 */
fun List<ArtistCreditName>?.getDisplayNames(): String {
    var displayName = ""
    this?.forEach {
        displayName += "${it.name}${it.joinPhrase.orEmpty()}"
    }
    return displayName
}
