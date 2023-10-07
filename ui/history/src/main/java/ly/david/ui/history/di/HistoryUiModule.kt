package ly.david.ui.history.di

import ly.david.ui.history.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val historyUiModule = module {
    viewModelOf(::HistoryViewModel)
}
