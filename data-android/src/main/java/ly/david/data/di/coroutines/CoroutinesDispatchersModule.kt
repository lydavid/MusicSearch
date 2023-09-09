package ly.david.data.di.coroutines

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

//@InstallIn(SingletonComponent::class)
//@Module
//object CoroutinesDispatchersModule {
//
//    @DefaultDispatcher
//    @Provides
//    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
//
//    @IoDispatcher
//    @Provides
//    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
//
//    @MainDispatcher
//    @Provides
//    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
//
//    @MainImmediateDispatcher
//    @Provides
//    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
//}

val coroutinesDispatchersModule = module {
    single(named("DefaultDispatcher")) { Dispatchers.Default }
    single(named("IoDispatcher")) { Dispatchers.IO }
    single(named("MainDispatcher")) { Dispatchers.Main }
    single(named("MainImmediateDispatcher")) { Dispatchers.Main.immediate }
}
