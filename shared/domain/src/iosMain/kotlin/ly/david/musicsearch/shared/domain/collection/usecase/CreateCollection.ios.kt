package ly.david.musicsearch.shared.domain.collection.usecase

import platform.Foundation.NSUUID

actual fun getUUID(): String {
    return NSUUID().UUIDString()
}
