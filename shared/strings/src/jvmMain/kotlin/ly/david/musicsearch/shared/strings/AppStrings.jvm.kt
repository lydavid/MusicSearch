package ly.david.musicsearch.shared.strings

actual fun String.fmt(vararg args: Any?): String {
    return format(*args)
}
