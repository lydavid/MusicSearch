package ly.david.data.di.logging

import ly.david.data.core.logging.Logger
import org.koin.dsl.module
import timber.log.Timber

//@InstallIn(SingletonComponent::class)
//@Module
//object LoggingModule {
//
//    @Singleton
//    @Provides
//    fun provideLogger(): Logger {
//        return object : Logger {
//            override fun e(exception: Exception) {
//                Timber.e(exception)
//            }
//        }
//    }
//}

val loggingModule = module {
    single<Logger> {
        object : Logger {
            override fun e(exception: Exception) {
                Timber.e(exception)
            }
        }
    }
}
