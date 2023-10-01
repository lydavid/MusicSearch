package ly.david.data.di.coroutines

import kotlinx.coroutines.Dispatchers
import ly.david.data.core.CoroutineDispatchers
import org.koin.dsl.module

val coroutineDispatchersModule = module {
    factory {
        CoroutineDispatchers(
            default = Dispatchers.Default,
            io = Dispatchers.IO,
        )
    }
}
