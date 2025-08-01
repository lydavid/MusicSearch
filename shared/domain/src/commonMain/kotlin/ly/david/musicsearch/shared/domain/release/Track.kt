package ly.david.musicsearch.shared.domain.release

/**
 * Represents a Track in a [Medium].
 */
interface Track {
    val id: String
    val position: Int
    val number: String
    val name: String
    val length: Int?
}
