package ly.david.musicsearch.data.core.release

/**
 * Represents a Track in a [Medium].
 */
interface Track {
    val id: String
    val position: Int
    val number: String
    val title: String
    val length: Int?
}
