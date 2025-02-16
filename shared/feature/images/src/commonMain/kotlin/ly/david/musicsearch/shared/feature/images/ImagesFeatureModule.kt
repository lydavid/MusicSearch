package ly.david.musicsearch.shared.feature.images

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val imagesFeatureModule = module {
    single(named("ImagesFeature")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is CoverArtsScreen -> {
                    CoverArtsPresenter(
                        screen = screen,
                        navigator = navigator,
                        appPreferences = get(),
                        releaseImageRepository = get(),
                        getMusicBrainzCoverArtUrl = get(),
                    )
                }

                else -> null
            }
        }
    }
    single(named("ImagesFeature")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is CoverArtsScreen -> {
                    ui<CoverArtsUiState> { state, modifier ->
                        CoverArtsGridUi(
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
