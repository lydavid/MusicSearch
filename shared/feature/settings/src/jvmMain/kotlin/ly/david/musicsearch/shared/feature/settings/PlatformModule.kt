package ly.david.musicsearch.shared.feature.settings

import ly.david.musicsearch.shared.feature.settings.internal.LoginPresenter
import ly.david.musicsearch.shared.feature.settings.internal.LoginPresenterJvm
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


internal actual val platformModule = module {
    singleOf(::LoginPresenterJvm) bind LoginPresenter::class
}
