package ly.david.data.common

inline fun <T> Collection<T>?.ifNotNullOrEmpty(block: (Collection<T>) -> Unit) {
    if (!this.isNullOrEmpty()) {
        block(this)
    }
}
