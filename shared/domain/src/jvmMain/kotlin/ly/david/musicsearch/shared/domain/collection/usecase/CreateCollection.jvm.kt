package ly.david.musicsearch.shared.domain.collection.usecase

import java.util.UUID

actual fun getUUID(): String {
    return UUID.randomUUID().toString()
}
