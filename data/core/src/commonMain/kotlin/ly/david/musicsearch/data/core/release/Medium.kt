package ly.david.musicsearch.data.core.release

/**
 * Represents a single medium in a release.
 *
 * If a release has 2 CDs and 1 Blu-Ray, it would have 3 media.
 */
interface Medium {
    val position: Int?
    val title: String?
    val trackCount: Int
    val format: String?
}
