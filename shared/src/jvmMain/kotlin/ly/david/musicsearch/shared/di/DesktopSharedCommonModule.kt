package ly.david.musicsearch.shared.di

import ly.david.musicsearch.domain.url.usecase.OpenInBrowser
import ly.david.musicsearch.shared.OpenInBrowserImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val sharedCommonModule: Module = module {
    singleOf(::OpenInBrowserImpl) bind OpenInBrowser::class
}
