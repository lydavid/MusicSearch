package ly.david.musicsearch.shared.domain.common

import platform.Foundation.NSUUID

actual fun getUUID(): String {
    return NSUUID().UUIDString()
}
