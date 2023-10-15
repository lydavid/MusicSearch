package ly.david.musicsearch.core.models

import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty

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
