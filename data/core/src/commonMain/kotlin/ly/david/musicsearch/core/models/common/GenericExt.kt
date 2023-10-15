package ly.david.musicsearch.core.models.common

inline fun <T> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}
