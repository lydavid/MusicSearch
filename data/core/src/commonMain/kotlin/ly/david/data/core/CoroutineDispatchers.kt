package ly.david.data.core

import kotlin.coroutines.CoroutineContext

data class CoroutineDispatchers(
    val default: CoroutineContext,
    val io: CoroutineContext,
)
