package ly.david.musicsearch.shared.feature.settings

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.settings.internal.Settings
import ly.david.musicsearch.shared.feature.settings.internal.SettingsPresenter
import ly.david.musicsearch.shared.feature.settings.internal.SettingsUiState
import ly.david.musicsearch.shared.feature.settings.internal.licenses.LicensesPresenter
import ly.david.musicsearch.shared.feature.settings.internal.licenses.LicensesScaffold
import ly.david.musicsearch.shared.feature.settings.internal.licenses.LicensesUiState
import ly.david.musicsearch.shared.screens.LicensesScreen
import ly.david.musicsearch.shared.screens.SettingsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsFeatureModule = module {
    single(named("SettingsFeaturePresenterFactories")) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is SettingsScreen -> SettingsPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                    get(),
                )

                is LicensesScreen -> LicensesPresenter(
                    navigator = navigator,
                )

                else -> null
            }
        }
    }
    single(named("SettingsFeatureUiFactories")) {
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

                is LicensesScreen -> {
                    ui<LicensesUiState> { state, modifier ->
                        LicensesScaffold(
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
