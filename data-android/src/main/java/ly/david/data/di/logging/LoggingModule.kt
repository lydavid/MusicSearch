package ly.david.data.di.logging

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.core.logging.Logger
import timber.log.Timber

@InstallIn(SingletonComponent::class)
@Module
object LoggingModule {

    @Singleton
    @Provides
    fun provideLogger(): Logger {
        return object : Logger {
            override fun e(exception: Exception) {
                Timber.e(exception)
            }
        }
    }
}
