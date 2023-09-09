package ly.david.data.di.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

//@InstallIn(SingletonComponent::class)
//@Module
//internal object CoroutinesScopesModule {
//
//    @Singleton
//    @ApplicationScope
//    @Provides
//    fun providesCoroutineScope(
//        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
//    ): CoroutineScope {
//        return CoroutineScope(SupervisorJob() + defaultDispatcher)
//    }
//}

val coroutinesScopesModule = module {
    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }
}
