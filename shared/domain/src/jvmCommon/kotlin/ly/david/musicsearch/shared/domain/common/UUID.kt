package ly.david.musicsearch.shared.domain.common

import java.util.UUID

actual fun getUUID(): String {
    return UUID.randomUUID().toString()
}
