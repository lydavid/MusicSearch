package ly.david.ui.history.di

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.ui.history.History
import ly.david.ui.history.HistoryPresenter
import ly.david.ui.history.HistoryScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val historyUiModule = module {
    single(named(HistoryPresenter::class.java.name)) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is HistoryScreen -> HistoryPresenter(
                    screen = screen,
                    navigator = navigator,
                    appPreferences = get(),
                    getPagedHistory = get(),
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
