package ly.david.musicsearch.shared.domain.common

inline fun <T> Collection<T>?.ifNotNullOrEmpty(block: (Collection<T>) -> Unit) {
    if (!this.isNullOrEmpty()) {
        block(this)
    }
}

inline fun <T> Collection<T>.ifNotEmpty(block: (Collection<T>) -> Unit) {
    if (this.isNotEmpty()) {
        block(this)
    }
}
