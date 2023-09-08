package ly.david.data.core.common

inline fun <T> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}
