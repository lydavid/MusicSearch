package ly.david.musicsearch.shared.feature.images

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.screen.CoverArtsGridScreen
import ly.david.musicsearch.ui.common.screen.CoverArtsPagerScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val imagesFeatureModule = module {
    single(named("ImagesFeature")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is CoverArtsGridScreen -> {
                    CoverArtsGridPresenter(
                        screen = screen,
                        navigator = navigator,
                        releaseImageRepository = get(),
                    )
                }

                is CoverArtsPagerScreen -> {
                    CoverArtsPagerPresenter(
                        screen = screen,
                        navigator = navigator,
                        releaseImageRepository = get(),
                    )
                }

                else -> null
            }
        }
    }
    single(named("ImagesFeature")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is CoverArtsGridScreen -> {
                    ui<CoverArtsGridUiState> { state, modifier ->
                        CoverArtsGridUi(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                is CoverArtsPagerScreen -> {
                    ui<CoverArtsPagerUiState> { state, modifier ->
                        CoverArtsPagerUi(
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
