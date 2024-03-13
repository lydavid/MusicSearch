package ly.david.musicsearch.shared.feature.details

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.details.area.AreaPresenter
import ly.david.musicsearch.shared.feature.details.area.AreaUi
import ly.david.musicsearch.shared.feature.details.area.AreaUiState
import ly.david.musicsearch.shared.feature.details.artist.ArtistPresenter
import ly.david.musicsearch.shared.feature.details.artist.ArtistUi
import ly.david.musicsearch.shared.feature.details.artist.ArtistUiState
import ly.david.ui.common.screen.DetailsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val detailsFeatureModule = module {
    single(named(DetailsScreen::class.java.name)) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is DetailsScreen -> {
                    when (screen.entity) {
                        MusicBrainzEntity.AREA -> {
                            AreaPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                placesByEntityPresenter = get(),
                                releasesByEntityPresenter = get(),
                                relationsPresenter = get(),
                                logger = get(),
                            )
                        }

                        MusicBrainzEntity.ARTIST -> {
                            ArtistPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                releaseGroupsByEntityPresenter = get(),
                                releasesByEntityPresenter = get(),
                                relationsPresenter = get(),
                                artistImageRepository = get(),
                                logger = get(),
                            )
                        }

                        else -> null
                    }
                }

                else -> null
            }
        }
    }
    single(named(DetailsScreen::class.java.name)) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is DetailsScreen -> {
                    when (screen.entity) {
                        MusicBrainzEntity.AREA -> {
                            ui<AreaUiState> { state, modifier ->
                                AreaUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.ARTIST -> {
                            ui<ArtistUiState> { state, modifier ->
                                ArtistUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        else -> null
                    }
                }

                else -> null
            }
        }
    }
}
