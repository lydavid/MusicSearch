package ly.david.musicsearch.core.preferences

internal inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T {
    return this?.let {
        try {
            enumValueOf<T>(it)
        } catch (_: IllegalArgumentException) {
            default
        }
    } ?: default
}
