package ly.david.musicsearch.core.coroutines

import kotlin.coroutines.CoroutineContext

data class CoroutineDispatchers(
    val default: CoroutineContext,
    val io: CoroutineContext,
)
