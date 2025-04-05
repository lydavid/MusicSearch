package ly.david.musicsearch.shared.feature.collections

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.collections.add.AddToCollectionPresenter
import ly.david.musicsearch.shared.feature.collections.add.AddToCollectionUi
import ly.david.musicsearch.shared.feature.collections.add.AddToCollectionUiState
import ly.david.musicsearch.shared.feature.collections.list.CollectionListUi
import ly.david.musicsearch.shared.feature.collections.list.CollectionListPresenter
import ly.david.musicsearch.shared.feature.collections.list.CollectionListUiState
import ly.david.musicsearch.shared.feature.collections.single.CollectionPresenter
import ly.david.musicsearch.shared.feature.collections.single.CollectionUi
import ly.david.musicsearch.shared.feature.collections.single.CollectionUiState
import ly.david.musicsearch.ui.common.screen.AddToCollectionScreen
import ly.david.musicsearch.ui.common.screen.CollectionListScreen
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val collectionsFeatureModule = module {
    single(named("CollectionListScreen")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is CollectionListScreen -> CollectionListPresenter(
                    screen = screen,
                    navigator = navigator,
                    appPreferences = get(),
                    getAllCollections = get(),
                    createCollection = get(),
                    deleteCollection = get(),
                )

                is CollectionScreen -> CollectionPresenter(
                    screen = screen,
                    navigator = navigator,
                    getCollection = get(),
                    incrementLookupHistory = get(),
                    areasListPresenter = get(),
                    artistsListPresenter = get(),
                    instrumentsListPresenter = get(),
                    labelsListPresenter = get(),
                    placesListPresenter = get(),
                    recordingsListPresenter = get(),
                    eventsListPresenter = get(),
                    genresListPresenter = get(),
                    releasesListPresenter = get(),
                    releaseGroupsListPresenter = get(),
                    worksListPresenter = get(),
                    seriesListPresenter = get(),
                    deleteFromCollection = get(),
                    getMusicBrainzUrl = get(),
                    collectionRepository = get(),
                )

                is AddToCollectionScreen -> AddToCollectionPresenter(
                    screen = screen,
                    navigator = navigator,
                    getAllCollections = get(),
                    createCollection = get(),
                    collectionRepository = get(),
                )

                else -> null
            }
        }
    }
    single(named("CollectionListScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is CollectionListScreen -> {
                    ui<CollectionListUiState> { state, modifier ->
                        CollectionListUi(
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

                is AddToCollectionScreen -> {
                    ui<AddToCollectionUiState> { state, modifier ->
                        AddToCollectionUi(
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
