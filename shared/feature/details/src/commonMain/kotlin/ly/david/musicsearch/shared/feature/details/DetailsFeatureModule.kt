package ly.david.musicsearch.shared.feature.details

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.feature.details.area.AreaPresenter
import ly.david.musicsearch.shared.feature.details.area.AreaUi
import ly.david.musicsearch.shared.feature.details.artist.ArtistPresenter
import ly.david.musicsearch.shared.feature.details.artist.ArtistUi
import ly.david.musicsearch.shared.feature.details.event.EventPresenter
import ly.david.musicsearch.shared.feature.details.event.EventUi
import ly.david.musicsearch.shared.feature.details.genre.GenrePresenter
import ly.david.musicsearch.shared.feature.details.genre.GenreUi
import ly.david.musicsearch.shared.feature.details.genre.GenreUiState
import ly.david.musicsearch.shared.feature.details.instrument.InstrumentPresenter
import ly.david.musicsearch.shared.feature.details.instrument.InstrumentUi
import ly.david.musicsearch.shared.feature.details.label.LabelPresenter
import ly.david.musicsearch.shared.feature.details.label.LabelUi
import ly.david.musicsearch.shared.feature.details.place.PlacePresenter
import ly.david.musicsearch.shared.feature.details.place.PlaceUi
import ly.david.musicsearch.shared.feature.details.recording.RecordingPresenter
import ly.david.musicsearch.shared.feature.details.recording.RecordingUi
import ly.david.musicsearch.shared.feature.details.release.ReleasePresenter
import ly.david.musicsearch.shared.feature.details.release.ReleaseUi
import ly.david.musicsearch.shared.feature.details.releasegroup.ReleaseGroupPresenter
import ly.david.musicsearch.shared.feature.details.releasegroup.ReleaseGroupUi
import ly.david.musicsearch.shared.feature.details.series.SeriesPresenter
import ly.david.musicsearch.shared.feature.details.series.SeriesUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.shared.feature.details.work.WorkPresenter
import ly.david.musicsearch.shared.feature.details.work.WorkUi
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val detailsFeatureModule = module {
    single(named("DetailsScreen")) {
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
                                entitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.ARTIST -> {
                            ArtistPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                wikimediaRepository = get(),
                                incrementLookupHistory = get(),
                                entitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                            )
                        }

                        MusicBrainzEntity.EVENT -> {
                            EventPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                entitiesListPresenter = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.GENRE -> {
                            GenrePresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                logger = get(),
                                getMusicBrainzUrl = get(),
                            )
                        }

                        MusicBrainzEntity.INSTRUMENT -> {
                            InstrumentPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                entitiesListPresenter = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.LABEL -> {
                            LabelPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                entitiesListPresenter = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.PLACE -> {
                            PlacePresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                entitiesListPresenter = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.RECORDING -> {
                            RecordingPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                entitiesListPresenter = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.RELEASE -> {
                            ReleasePresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                entitiesListPresenter = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.RELEASE_GROUP -> {
                            ReleaseGroupPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                entitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.SERIES -> {
                            SeriesPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                entitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        MusicBrainzEntity.WORK -> {
                            WorkPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                entitiesListPresenter = get(),
                                logger = get(),
                                loginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                            )
                        }

                        else -> null
                    }
                }

                else -> null
            }
        }
    }
    single(named("DetailsScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is DetailsScreen -> {
                    when (screen.entity) {
                        MusicBrainzEntity.AREA -> {
                            ui<DetailsUiState<AreaDetailsModel>> { state, modifier ->
                                AreaUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.ARTIST -> {
                            ui<DetailsUiState<ArtistDetailsModel>> { state, modifier ->
                                ArtistUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.EVENT -> {
                            ui<DetailsUiState<EventDetailsModel>> { state, modifier ->
                                EventUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.GENRE -> {
                            ui<GenreUiState> { state, modifier ->
                                GenreUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.INSTRUMENT -> {
                            ui<DetailsUiState<InstrumentDetailsModel>> { state, modifier ->
                                InstrumentUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.LABEL -> {
                            ui<DetailsUiState<LabelDetailsModel>> { state, modifier ->
                                LabelUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.PLACE -> {
                            ui<DetailsUiState<PlaceDetailsModel>> { state, modifier ->
                                PlaceUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.RECORDING -> {
                            ui<DetailsUiState<RecordingDetailsModel>> { state, modifier ->
                                RecordingUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.RELEASE -> {
                            ui<DetailsUiState<ReleaseDetailsModel>> { state, modifier ->
                                ReleaseUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.RELEASE_GROUP -> {
                            ui<DetailsUiState<ReleaseGroupDetailsModel>> { state, modifier ->
                                ReleaseGroupUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.SERIES -> {
                            ui<DetailsUiState<SeriesDetailsModel>> { state, modifier ->
                                SeriesUi(
                                    state = state,
                                    entityId = screen.id,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntity.WORK -> {
                            ui<DetailsUiState<WorkDetailsModel>> { state, modifier ->
                                WorkUi(
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
