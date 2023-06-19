package ly.david.data.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.data.common.ApplicationScope

@InstallIn(SingletonComponent::class)
@Module
internal object CoroutinesScopesModule {

    @Singleton // Provide always the same instance
    @ApplicationScope
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + defaultDispatcher)
    }
}
