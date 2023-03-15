package ly.david.mbjc.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.auth.MusicBrainzAuthState

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val appPreferences: AppPreferences,
    val musicBrainzAuthState: MusicBrainzAuthState,
) : ViewModel()
