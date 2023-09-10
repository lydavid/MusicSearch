package ly.david.mbjc.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.createTestCoroutineScope
import ly.david.data.di.coroutines.MusicSearchDispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

//@OptIn(ExperimentalCoroutinesApi::class)
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [CoroutinesDispatchersModule::class]
//)
//internal object TestCoroutinesDispatchersModule {
//    @DefaultDispatcher
//    @Provides
//    fun providesDefaultDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
//
//    @IoDispatcher
//    @Provides
//    fun providesIoDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
//
//    @MainDispatcher
//    @Provides
//    fun providesMainDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
//
//    @MainImmediateDispatcher
//    @Provides
//    fun providesMainImmediateDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
//}

val testCoroutineDispatchersModule = module {
    factory<CoroutineDispatcher>(named(MusicSearchDispatchers.Default)) {
        Dispatchers.Unconfined
    }
    factory<CoroutineDispatcher>(named(MusicSearchDispatchers.IO)) {
        Dispatchers.Unconfined
    }
}

val testCoroutinesScopesModule = module {
    single<CoroutineScope> {
        createTestCoroutineScope(TestCoroutineDispatcher())
////        TestCoroutineScope()
////        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(MusicSearchDispatchers.Default)))
    }
}
