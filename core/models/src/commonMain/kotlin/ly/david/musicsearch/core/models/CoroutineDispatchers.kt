package ly.david.musicsearch.core.models

import kotlin.coroutines.CoroutineContext

data class CoroutineDispatchers(
    val default: CoroutineContext,
    val io: CoroutineContext,
)
