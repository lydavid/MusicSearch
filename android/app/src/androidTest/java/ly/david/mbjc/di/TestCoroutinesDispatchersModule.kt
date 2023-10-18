package ly.david.mbjc.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ly.david.musicsearch.core.models.CoroutineDispatchers
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val testCoroutineDispatchersModule = module {
    factory {
        val testDispatcher = UnconfinedTestDispatcher()
        CoroutineDispatchers(
            default = testDispatcher,
            io = testDispatcher,
        )
    }
}
