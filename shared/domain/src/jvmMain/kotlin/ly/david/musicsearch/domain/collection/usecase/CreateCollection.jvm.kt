package ly.david.musicsearch.domain.collection.usecase

import java.util.UUID

actual fun getUUID(): String {
    return UUID.randomUUID().toString()
}
