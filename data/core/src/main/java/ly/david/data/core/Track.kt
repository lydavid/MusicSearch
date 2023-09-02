package ly.david.data.core

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
