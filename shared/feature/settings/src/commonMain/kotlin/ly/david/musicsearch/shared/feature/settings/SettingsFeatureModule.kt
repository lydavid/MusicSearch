package ly.david.musicsearch.shared.feature.settings

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.settings.internal.SettingsPresenter
import ly.david.musicsearch.shared.feature.settings.internal.SettingsUi
import ly.david.musicsearch.shared.feature.settings.internal.SettingsUiState
import ly.david.musicsearch.shared.feature.settings.internal.appearance.AppearanceSettingsPresenter
import ly.david.musicsearch.shared.feature.settings.internal.appearance.AppearanceSettingsUi
import ly.david.musicsearch.shared.feature.settings.internal.appearance.AppearanceSettingsUiState
import ly.david.musicsearch.shared.feature.settings.internal.images.ImagesSettingsPresenter
import ly.david.musicsearch.shared.feature.settings.internal.images.ImagesSettingsUi
import ly.david.musicsearch.shared.feature.settings.internal.images.ImagesSettingsUiState
import ly.david.musicsearch.ui.common.screen.AppearanceSettingsScreen
import ly.david.musicsearch.ui.common.screen.ImagesSettingsScreen
import ly.david.musicsearch.ui.common.screen.SettingsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsFeatureModule = module {
    single(named("SettingsScreen")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is SettingsScreen -> SettingsPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                    musicBrainzAuthStore = get(),
                    loginPresenter = get(),
                    logout = get(),
                    exportDatabase = get(),
                    metadataRepository = get(),
                )

                is AppearanceSettingsScreen -> AppearanceSettingsPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                )

                is ImagesSettingsScreen -> ImagesSettingsPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                )

                else -> null
            }
        }
    }
    single(named("SettingsScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is SettingsScreen -> {
                    ui<SettingsUiState> { state, modifier ->
                        SettingsUi(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                is AppearanceSettingsScreen -> {
                    ui<AppearanceSettingsUiState> { state, modifier ->
                        AppearanceSettingsUi(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                is ImagesSettingsScreen -> {
                    ui<ImagesSettingsUiState> { state, modifier ->
                        ImagesSettingsUi(
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
