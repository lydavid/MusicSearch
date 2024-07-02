package ly.david.musicsearch.shared.di

import ly.david.musicsearch.android.feature.nowplaying.nowPlayingFeatureModule
import ly.david.musicsearch.android.feature.spotify.spotifyFeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    includes(
        nowPlayingFeatureModule,
        spotifyFeatureModule,
    )
}
