package ly.david.musicsearch.data.database.dao

import java.net.URLDecoder

internal actual fun String.decodeUrl(): String {
    return URLDecoder.decode(this, "utf-8")
}
