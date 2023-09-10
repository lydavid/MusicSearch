package ly.david.data.di.coroutines

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutineDispatchersModule = module {
    factory(named(MusicSearchDispatchers.Default)) {
        Dispatchers.Default
    }
    factory(named(MusicSearchDispatchers.IO)) {
        Dispatchers.IO
    }
}
