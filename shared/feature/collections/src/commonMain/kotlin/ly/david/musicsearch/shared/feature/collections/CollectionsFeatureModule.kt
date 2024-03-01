package ly.david.musicsearch.shared.feature.collections

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.collections.internal.CollectionList
import ly.david.musicsearch.shared.feature.collections.internal.CollectionListPresenter
import ly.david.musicsearch.shared.feature.collections.internal.CollectionListUiState
import ly.david.musicsearch.shared.screens.CollectionListScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val collectionsFeatureModule = module {
    single(named(CollectionListScreen::class.java.name)) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is CollectionListScreen -> CollectionListPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                    getAllCollections = get(),
                )

                else -> null
            }
        }
    }
    single(named(CollectionListScreen::class.java.name)) {
        Ui.Factory { screen, context ->
            when (screen) {
                is CollectionListScreen -> {
                    ui<CollectionListUiState> { state, modifier ->
                        CollectionList(
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
