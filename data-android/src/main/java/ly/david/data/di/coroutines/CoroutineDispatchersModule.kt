package ly.david.data.di.coroutines

import kotlinx.coroutines.Dispatchers
import ly.david.musicsearch.core.models.CoroutineDispatchers
import org.koin.dsl.module

val coroutineDispatchersModule = module {
    factory {
        CoroutineDispatchers(
            default = Dispatchers.Default,
            io = Dispatchers.IO,
        )
    }
}
