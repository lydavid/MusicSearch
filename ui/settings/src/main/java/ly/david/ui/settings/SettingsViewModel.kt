package ly.david.ui.settings

import androidx.lifecycle.ViewModel
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthStore
import ly.david.musicsearch.core.preferences.AppPreferences
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    val appPreferences: AppPreferences,
    val musicBrainzAuthStore: MusicBrainzAuthStore,
) : ViewModel()
