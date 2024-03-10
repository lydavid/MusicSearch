package ly.david.musicsearch.shared.feature.details

import androidx.compose.material3.Text
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.details.area.AreaPresenter
import ly.david.musicsearch.shared.feature.details.area.AreaUi
import ly.david.musicsearch.shared.feature.details.area.AreaUiState
import ly.david.musicsearch.shared.feature.details.area.ReleasesByEntityPresenter
import ly.david.musicsearch.shared.feature.details.area.ReleasesByEntityUiState
import ly.david.musicsearch.shared.screens.DetailsScreen
import ly.david.musicsearch.shared.screens.ReleasesByEntityScreen
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val detailsFeatureModule = module {
    singleOf(::ReleasesByEntityPresenter)

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
                                getPlacesByEntity = get(),
                                releasesByEntityPresenter = get(),
                                relationsList = get(),
                                releaseGroupImageRepository = get(),
                                logger = get(),
                            )
                        }

                        else -> null
                    }
                }
//                is ReleasesByEntityScreen -> {
//                    ReleasesByEntityPresenter(
//                        screen = screen,
//                        getReleasesByEntity = get(),
//                        appPreferences = get(),
//                        releaseImageRepository = get(),
//                    )
//                }

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

                        else -> null
                    }
                }

//                is ReleasesByEntityScreen -> {
//                    ui<ReleasesByEntityUiState> { state, modifier ->
//                        Text("e")
//                    }
//                }

                else -> null
            }
        }
    }
}
