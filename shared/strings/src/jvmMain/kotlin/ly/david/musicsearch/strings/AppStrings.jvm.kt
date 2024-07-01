package ly.david.musicsearch.strings

actual fun String.fmt(vararg args: Any?): String {
    return format(*args)
}
