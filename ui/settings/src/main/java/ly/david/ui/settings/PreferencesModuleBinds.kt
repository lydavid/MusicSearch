package ly.david.ui.settings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class PreferencesModuleBinds {
    // Shows as unused but it is indeed used. Do not remove!
    @Singleton
    @Binds
    abstract fun provideAppPreferences(bind: AppPreferencesImpl): AppPreferences
}
