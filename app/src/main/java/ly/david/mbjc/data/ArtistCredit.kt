package ly.david.mbjc.data

interface ArtistCredit {
    val name: String
    val joinPhrase: String?
}

/**
 * Get all artists in the credit, joined by their [ArtistCredit.joinPhrase].
 */
fun List<ArtistCredit>?.getDisplayNames(): String {
    var displayName = ""
    this?.forEach {
        displayName += "${it.name}${it.joinPhrase.orEmpty()}"
    }
    return displayName
}
