package ly.david.musicsearch.core.preferences.di

import org.koin.core.module.Module

internal const val SETTINGS_KEY = "settings"
internal const val DATASTORE_FILE_NAME = "$SETTINGS_KEY.preferences_pb"

expect val preferencesDataStoreModule: Module
