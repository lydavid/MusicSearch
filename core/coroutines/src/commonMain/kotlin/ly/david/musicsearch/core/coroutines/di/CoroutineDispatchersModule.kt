package ly.david.musicsearch.core.coroutines.di

import kotlinx.coroutines.Dispatchers
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import org.koin.dsl.module

val coroutineDispatchersModule = module {
    factory {
        CoroutineDispatchers(
            default = Dispatchers.Default,
            io = Dispatchers.IO,
        )
    }
}
