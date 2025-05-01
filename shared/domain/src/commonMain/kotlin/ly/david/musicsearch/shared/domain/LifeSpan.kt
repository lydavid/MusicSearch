package ly.david.musicsearch.shared.domain

interface LifeSpan {
    val begin: String?
    val end: String?
    val ended: Boolean?
}

fun LifeSpan?.getLifeSpanForDisplay(): String {
    return when {
        this == null -> {
            ""
        }

        begin.isNullOrEmpty() && end.isNullOrEmpty() -> {
            ""
        }

        begin.isNullOrEmpty() && !end.isNullOrEmpty() -> {
            "?? - $end"
        }

        !begin.isNullOrEmpty() && end.isNullOrEmpty() -> {
            "$begin -"
        }

        begin == end -> {
            "$begin"
        }

        else -> {
            "$begin - $end"
        }
    }
}
