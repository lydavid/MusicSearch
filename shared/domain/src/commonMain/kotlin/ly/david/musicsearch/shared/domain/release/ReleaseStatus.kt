package ly.david.musicsearch.shared.domain.release

@Suppress("MagicNumber")
enum class ReleaseStatus(val order: Int) {
    OFFICIAL(0),
    PROMOTION(1),
    BOOTLEG(2),
    PSEUDO_RELEASE(3),
    WITHDRAWN(4),
    EXPUNGED(5),
    CANCELLED(6),
    UNKNOWN(7),
}
