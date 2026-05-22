package ly.david.musicsearch.shared.di

import ly.david.musicsearch.shared.domain.app.AndroidBuildConfig
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single {
        AndroidBuildConfig(
            isDebug = false,
        )
    }
}
