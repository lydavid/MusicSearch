package ly.david.musicsearch.shared.di

import ly.david.musicsearch.android.feature.nowplaying.nowPlayingFeatureModule
import ly.david.musicsearch.android.feature.spotify.spotifyFeatureModule
import ly.david.musicsearch.domain.DomainModule
import ly.david.musicsearch.ui.image.di.imageModule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

actual val platformModule: Module = module {
    includes(
        nowPlayingFeatureModule,
        spotifyFeatureModule,
        imageModule,

        DomainModule().module,
    )
}
