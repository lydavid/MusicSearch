package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ly.david.data.coroutines.CoroutinesDispatchersModule
import ly.david.data.coroutines.DefaultDispatcher
import ly.david.data.coroutines.IoDispatcher
import ly.david.data.coroutines.MainDispatcher
import ly.david.data.coroutines.MainImmediateDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoroutinesDispatchersModule::class]
)
internal object TestCoroutinesDispatchersModule {
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
}
