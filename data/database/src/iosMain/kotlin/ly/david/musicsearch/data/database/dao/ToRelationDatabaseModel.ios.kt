package ly.david.musicsearch.data.database.dao

import platform.Foundation.NSString
import platform.Foundation.stringByRemovingPercentEncoding

// https://kotlinlang.org/docs/native-objc-interop.html#casting-between-mapped-types
@Suppress("CAST_NEVER_SUCCEEDS")
internal actual fun String.decodeUrl(): String {
    return (this as NSString).stringByRemovingPercentEncoding ?: this
}
