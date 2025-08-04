package ly.david.musicsearch.shared.feature.details.utils

internal fun getNumberOfFilteredItems(
    filteredCount: Int,
    total: Int,
): String {
    return buildString {
        append("(")
        append(
            if (filteredCount == total) {
                filteredCount
            } else {
                "$filteredCount / $total"
            },
        )
        append(")")
    }
}
