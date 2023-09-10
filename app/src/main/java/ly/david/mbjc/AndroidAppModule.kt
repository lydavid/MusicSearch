package ly.david.mbjc

import ly.david.data.coverart.CoverArtDataModule
import ly.david.data.di.auth.AuthStoreModule
import ly.david.data.di.coroutines.coroutinesScopesModule
import ly.david.data.di.logging.loggingModule
import ly.david.data.di.musicbrainz.musicBrainzAuthModule
import ly.david.data.di.network.networkModule
import ly.david.data.di.preferences.preferencesDataStoreModule
import ly.david.data.di.room.databaseDaoModule
import ly.david.data.di.room.databaseModule
import ly.david.data.domain.DomainDataModule
import ly.david.data.musicbrainz.auth.MusicBrainzDataModule
import ly.david.data.spotify.di.SpotifyDataModule
import ly.david.mbjc.di.appDataModule
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.HistoryUiModule
import ly.david.ui.image.di.imageModule
import ly.david.ui.nowplaying.NowPlayingUiModule
import ly.david.ui.settings.SettingsUiModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val androidAppModule = module {
    includes(
        ViewModelsModule().module,
        appDataModule,
        coroutinesScopesModule,
        loggingModule,
        musicBrainzAuthModule,
        networkModule,
        preferencesDataStoreModule,
        databaseDaoModule,
        databaseModule,
        imageModule,
        CoverArtDataModule().module,
        DomainDataModule().module,
        MusicBrainzDataModule().module,
        SpotifyDataModule().module,
        AuthStoreModule().module,
        CollectionUiModule().module,
        CommonUiModule().module,
        HistoryUiModule().module,
        NowPlayingUiModule().module,
        SettingsUiModule().module,
    )
}
