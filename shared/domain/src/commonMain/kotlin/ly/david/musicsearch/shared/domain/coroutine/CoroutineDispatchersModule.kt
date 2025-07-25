package ly.david.musicsearch.shared.domain.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

internal val coroutineDispatchersModule = module {
    single<CoroutineDispatchers> {
        CoroutineDispatchers(
            default = Dispatchers.Default,
            io = Dispatchers.IO,
        )
    }
}
