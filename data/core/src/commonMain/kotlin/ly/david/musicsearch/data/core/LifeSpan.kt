package ly.david.musicsearch.data.core

import ly.david.musicsearch.data.core.common.transformThisIfNotNullOrEmpty

interface LifeSpan {
    val begin: String?
    val end: String?
    val ended: Boolean?
}

fun LifeSpan?.getLifeSpanForDisplay(): String {
    if (this == null) return ""
    val begin = begin.orEmpty()
    val end = if (begin == end) {
        ""
    } else {
        end.transformThisIfNotNullOrEmpty { " to $it" }
    }
    return begin + end
}
