package ly.david.musicsearch.feature.stats

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.feature.stats.internal.StatsUiState
import ly.david.ui.common.screen.StatsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statsFeatureModule = module {
    single(named(StatsScreen::class.java.name)) {
        Presenter.Factory { screen, _, _ ->
            when (screen) {
                is StatsScreen -> {
                    when (screen.entity) {
                        MusicBrainzEntity.AREA -> {
                            AreaStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                releaseCountryDao = get(),
                                areaPlaceDao = get(),
                            )
                        }

                        MusicBrainzEntity.ARTIST -> {
                            ArtistStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                artistReleaseGroupDao = get(),
                                artistReleaseDao = get(),
                            )
                        }

                        MusicBrainzEntity.EVENT -> {
                            EventStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                            )
                        }

                        MusicBrainzEntity.INSTRUMENT -> {
                            InstrumentStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                            )
                        }

                        MusicBrainzEntity.LABEL -> {
                            LabelStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                releaseLabelDao = get(),
                            )
                        }

                        MusicBrainzEntity.PLACE -> {
                            PlaceStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                eventPlaceDao = get(),
                            )
                        }

                        MusicBrainzEntity.RECORDING -> {
                            RecordingStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                recordingReleaseDao = get(),
                            )
                        }

                        MusicBrainzEntity.RELEASE_GROUP -> {
                            ReleaseGroupStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                releaseReleaseGroupDao = get(),
                            )
                        }

                        else -> null
                    }
                }

                else -> null
            }
        }
    }
    single(named(StatsScreen::class.java.name)) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is StatsScreen -> {
                    ui<StatsUiState> { state, modifier ->
                        StatsUi(
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
