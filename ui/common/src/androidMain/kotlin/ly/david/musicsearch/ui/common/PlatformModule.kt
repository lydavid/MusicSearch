package ly.david.musicsearch.ui.common

import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenterAndroid
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule = module {
    singleOf(::LoginPresenterAndroid) bind LoginPresenter::class
}
