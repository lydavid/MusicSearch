package ly.david.data.common

inline fun <T> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}
