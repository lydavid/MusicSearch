package ly.david.musicsearch.shared.feature.settings

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.settings.internal.Settings
import ly.david.musicsearch.shared.feature.settings.internal.SettingsPresenter
import ly.david.musicsearch.shared.feature.settings.internal.SettingsUiState
import ly.david.musicsearch.shared.screens.SettingsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsFeatureModule = module {
    single(named(SettingsScreen::class.java.name)) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is SettingsScreen -> SettingsPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                    get(),
                )

                else -> null
            }
        }
    }
    single(named(SettingsScreen::class.java.name)) {
        Ui.Factory { screen, context ->
            when (screen) {
                is SettingsScreen -> {
                    ui<SettingsUiState> { state, modifier ->
                        Settings(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                else -> null
            }
        }
    }
}
