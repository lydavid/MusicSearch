package ly.david.musicsearch.shared.feature.history

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.history.internal.History
import ly.david.musicsearch.shared.feature.history.internal.HistoryPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val historyFeatureModule = module {
    single(named(HistoryPresenter::class.java.name)) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is HistoryScreen -> HistoryPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                    getPagedHistory = get(),
                    markLookupHistoryForDeletion = get(),
                    unMarkLookupHistoryForDeletion = get(),
                    deleteLookupHistory = get(),
                )

                else -> null
            }
        }
    }
    single(named(HistoryScreen::class.java.name)) {
        Ui.Factory { screen, context ->
            when (screen) {
                is HistoryScreen -> {
                    ui<HistoryScreen.UiState> { state, modifier ->
                        History(
                            uiState = state,
                            modifier = modifier,
                        )
                    }
                }

                else -> null
            }
        }
    }
}
