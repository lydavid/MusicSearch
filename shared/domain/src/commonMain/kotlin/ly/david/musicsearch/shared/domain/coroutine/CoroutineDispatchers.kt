package ly.david.musicsearch.shared.domain.coroutine

import kotlin.coroutines.CoroutineContext

data class CoroutineDispatchers(
    val default: CoroutineContext,
    val io: CoroutineContext,
)
