package ly.david.ui.settings

import androidx.lifecycle.ViewModel
import ly.david.data.musicbrainz.auth.MusicBrainzAuthStore
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    val appPreferences: AppPreferences,
    val musicBrainzAuthStore: MusicBrainzAuthStore,
) : ViewModel()
