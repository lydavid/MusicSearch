package ly.david.musicsearch.shared.domain.common

import java.net.URLDecoder

actual fun String.decodeUrl(): String {
    return URLDecoder.decode(this, "UTF-8")
}
