package ly.david.musicsearch.ui.common

import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginPresenterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule = module {
    singleOf(::MusicBrainzLoginPresenterImpl) bind MusicBrainzLoginPresenter::class
}
