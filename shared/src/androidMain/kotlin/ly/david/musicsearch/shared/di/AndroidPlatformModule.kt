package ly.david.musicsearch.shared.di

import ly.david.musicsearch.android.feature.nowplaying.NowPlayingUiModule
import ly.david.musicsearch.domain.DomainModule
import ly.david.musicsearch.ui.image.di.imageModule
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.di.historyUiModule
import ly.david.ui.settings.SettingsUiModule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

actual val platformModule: Module = module {
    includes(
        CollectionUiModule().module,
        CommonUiModule().module,
        historyUiModule,
        NowPlayingUiModule().module,
        SettingsUiModule().module,
        imageModule,

        DomainModule().module,
    )
}
