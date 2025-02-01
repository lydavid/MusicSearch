package ly.david.musicsearch.core.preferences.di

import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.core.preferences.AppPreferencesImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appPreferencesModule = module {
    singleOf(::AppPreferencesImpl) bind AppPreferences::class
}
