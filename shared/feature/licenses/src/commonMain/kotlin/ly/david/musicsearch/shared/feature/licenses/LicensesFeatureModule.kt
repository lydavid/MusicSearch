package ly.david.musicsearch.shared.feature.licenses

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.licenses.internal.LicensesUi
import ly.david.musicsearch.shared.feature.licenses.internal.LicensesPresenter
import ly.david.musicsearch.shared.feature.licenses.internal.LicensesUiState
import ly.david.musicsearch.ui.common.screen.LicensesScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val licensesFeatureModule = module {
    single(named("LicensesScreen")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is LicensesScreen -> LicensesPresenter(
                    navigator = navigator,
                )

                else -> null
            }
        }
    }
    single(named("LicensesScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is LicensesScreen -> {
                    ui<LicensesUiState> { state, modifier ->
                        LicensesUi(
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
