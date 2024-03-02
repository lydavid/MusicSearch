package ly.david.musicsearch.shared.feature.collections

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.collections.list.CollectionList
import ly.david.musicsearch.shared.feature.collections.list.CollectionListPresenter
import ly.david.musicsearch.shared.feature.collections.list.CollectionListUiState
import ly.david.musicsearch.shared.feature.collections.single.CollectionPresenter
import ly.david.musicsearch.shared.feature.collections.single.CollectionUi
import ly.david.musicsearch.shared.feature.collections.single.CollectionUiState
import ly.david.musicsearch.shared.screens.CollectionListScreen
import ly.david.musicsearch.shared.screens.CollectionScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val collectionsFeatureModule = module {
    single(named(CollectionListScreen::class.java.name)) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is CollectionListScreen -> CollectionListPresenter(
                    navigator = navigator,
                    appPreferences = get(),
                    getAllCollections = get(),
                    createCollection = get(),
                )

                is CollectionScreen -> CollectionPresenter(
                    screen = screen,
                    navigator = navigator,
                    getCollectionUseCase = get(),
                    incrementLookupHistory = get(),
                )

                else -> null
            }
        }
    }
    single(named(CollectionListScreen::class.java.name)) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is CollectionListScreen -> {
                    ui<CollectionListUiState> { state, modifier ->
                        CollectionList(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                is CollectionScreen -> {
                    ui<CollectionUiState> { state, modifier ->
                        CollectionUi(
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
