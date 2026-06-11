package ly.david.musicsearch.shared.domain.common

inline fun <T> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}

inline fun <T> T?.transformOrEmptyIfNull(block: (T) -> String): String {
    return if (this == null) {
        ""
    } else {
        block(this)
    }
}
