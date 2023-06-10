package ly.david.mbjc.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.ui.settings.AppPreferences
import ly.david.ui.settings.AppPreferencesImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class PreferencesModuleBinds {
    // Shows as unused but it is indeed used. Do not remove!
    @Singleton
    @Binds
    abstract fun provideAppPreferences(bind: AppPreferencesImpl): AppPreferences
}
