package ly.david.mbjc.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ly.david.data.di.coroutines.MusicSearchDispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val testDispatcherModule = module {
    single {
        UnconfinedTestDispatcher()
    }
}

val testCoroutineDispatchersModule = module {
    includes(testDispatcherModule)
    factory<CoroutineDispatcher>(named(MusicSearchDispatchers.Default)) {
        get<TestDispatcher>()
    }
    factory<CoroutineDispatcher>(named(MusicSearchDispatchers.IO)) {
        get<TestDispatcher>()
    }
}
