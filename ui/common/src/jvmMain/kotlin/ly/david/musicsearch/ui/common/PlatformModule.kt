package ly.david.musicsearch.ui.common

import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenterJvm
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule = module {
    singleOf(::LoginPresenterJvm) bind LoginPresenter::class
}
