package ly.david.musicsearch.shared.di

import ly.david.musicsearch.domain.url.usecase.OpenInBrowser
import ly.david.musicsearch.shared.OpenInBrowserImpl
import ly.david.musicsearch.domain.DomainModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ksp.generated.module

actual val platformModule: Module = module {
    singleOf(::OpenInBrowserImpl) bind OpenInBrowser::class
    includes(
        DomainModule().module,
    )
}
