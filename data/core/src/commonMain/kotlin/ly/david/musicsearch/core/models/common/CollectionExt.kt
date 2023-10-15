package ly.david.musicsearch.core.models.common

inline fun <T> Collection<T>?.ifNotNullOrEmpty(block: (Collection<T>) -> Unit) {
    if (!this.isNullOrEmpty()) {
        block(this)
    }
}
