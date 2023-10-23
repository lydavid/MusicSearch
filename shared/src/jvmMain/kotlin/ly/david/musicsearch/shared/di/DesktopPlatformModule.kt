package ly.david.musicsearch.shared.di

import ly.david.musicsearch.domain.DomainModule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

actual val platformModule: Module = module {
    includes(
        DomainModule().module,
    )
}
