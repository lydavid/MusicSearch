package ly.david.musicsearch.shared.feature.details

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
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
                    when (screen.entityType) {
                        MusicBrainzEntityType.AREA -> {
                            AreaPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                allEntitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.ARTIST -> {
                            ArtistPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                                incrementLookupHistory = get(),
                                allEntitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                            )
                        }

                        MusicBrainzEntityType.EVENT -> {
                            EventPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                allEntitiesListPresenter = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.GENRE -> {
                            GenrePresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                logger = get(),
                                getMusicBrainzUrl = get(),
                            )
                        }

                        MusicBrainzEntityType.INSTRUMENT -> {
                            InstrumentPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                allEntitiesListPresenter = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.LABEL -> {
                            LabelPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                allEntitiesListPresenter = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.PLACE -> {
                            PlacePresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                allEntitiesListPresenter = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.RECORDING -> {
                            RecordingPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                allEntitiesListPresenter = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.RELEASE -> {
                            ReleasePresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                allEntitiesListPresenter = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.RELEASE_GROUP -> {
                            ReleaseGroupPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                allEntitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.SERIES -> {
                            SeriesPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                allEntitiesListPresenter = get(),
                                imageMetadataRepository = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
                            )
                        }

                        MusicBrainzEntityType.WORK -> {
                            WorkPresenter(
                                screen = screen,
                                navigator = navigator,
                                repository = get(),
                                incrementLookupHistory = get(),
                                imageMetadataRepository = get(),
                                allEntitiesListPresenter = get(),
                                logger = get(),
                                musicBrainzLoginPresenter = get(),
                                getMusicBrainzUrl = get(),
                                wikimediaRepository = get(),
                                collectionRepository = get(),
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
                    when (screen.entityType) {
                        MusicBrainzEntityType.AREA -> {
                            ui<DetailsUiState<AreaDetailsModel>> { state, modifier ->
                                AreaUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.ARTIST -> {
                            ui<DetailsUiState<ArtistDetailsModel>> { state, modifier ->
                                ArtistUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.EVENT -> {
                            ui<DetailsUiState<EventDetailsModel>> { state, modifier ->
                                EventUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.GENRE -> {
                            ui<GenreUiState> { state, modifier ->
                                GenreUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.INSTRUMENT -> {
                            ui<DetailsUiState<InstrumentDetailsModel>> { state, modifier ->
                                InstrumentUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.LABEL -> {
                            ui<DetailsUiState<LabelDetailsModel>> { state, modifier ->
                                LabelUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.PLACE -> {
                            ui<DetailsUiState<PlaceDetailsModel>> { state, modifier ->
                                PlaceUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.RECORDING -> {
                            ui<DetailsUiState<RecordingDetailsModel>> { state, modifier ->
                                RecordingUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.RELEASE -> {
                            ui<DetailsUiState<ReleaseDetailsModel>> { state, modifier ->
                                ReleaseUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.RELEASE_GROUP -> {
                            ui<DetailsUiState<ReleaseGroupDetailsModel>> { state, modifier ->
                                ReleaseGroupUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.SERIES -> {
                            ui<DetailsUiState<SeriesDetailsModel>> { state, modifier ->
                                SeriesUi(
                                    state = state,
                                    modifier = modifier,
                                )
                            }
                        }

                        MusicBrainzEntityType.WORK -> {
                            ui<DetailsUiState<WorkDetailsModel>> { state, modifier ->
                                WorkUi(
                                    state = state,
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
