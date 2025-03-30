package ly.david.musicsearch.shared.feature.stats

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.stats.internal.StatsUiState
import ly.david.musicsearch.ui.common.screen.StatsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statsFeatureModule = module {
    single(named("StatsScreen")) {
        Presenter.Factory { screen, _, _ ->
            when (screen) {
                is StatsScreen -> {
                    when (screen.entity) {
                        MusicBrainzEntity.AREA -> {
                            AreaStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                releaseDao = get(),
                                artistDao = get(),
                                eventDao = get(),
                                labelDao = get(),
                                placeDao = get(),
                            )
                        }

                        MusicBrainzEntity.ARTIST -> {
                            ArtistStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                eventDao = get(),
                                releaseGroupDao = get(),
                                releaseDao = get(),
                                recordingDao = get(),
                                workDao = get(),
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
                                releaseDao = get(),
                            )
                        }

                        MusicBrainzEntity.PLACE -> {
                            PlaceStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                eventDao = get(),
                            )
                        }

                        MusicBrainzEntity.RECORDING -> {
                            RecordingStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                releaseDao = get(),
                            )
                        }

                        MusicBrainzEntity.RELEASE -> {
                            ReleaseStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                artistDao = get(),
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

                        MusicBrainzEntity.SERIES -> {
                            SeriesStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                            )
                        }

                        MusicBrainzEntity.WORK -> {
                            WorkStatsPresenter(
                                screen = screen,
                                getCountOfEachRelationshipTypeUseCase = get(),
                                observeBrowseEntityCount = get(),
                                artistDao = get(),
                                recording = get(),
                            )
                        }

                        else -> null
                    }
                }

                else -> null
            }
        }
    }
    single(named("StatsScreen")) {
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
