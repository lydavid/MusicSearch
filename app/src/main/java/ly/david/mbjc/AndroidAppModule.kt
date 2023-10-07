package ly.david.mbjc

import ly.david.data.coverart.CoverArtDataModule
import ly.david.data.di.auth.AuthStoreModule
import ly.david.data.di.coroutines.coroutineDispatchersModule
import ly.david.data.di.coroutines.coroutinesScopesModule
import ly.david.data.di.logging.loggingModule
import ly.david.data.di.musicbrainz.musicBrainzAuthModule
import ly.david.data.di.network.networkModule
import ly.david.data.di.preferences.preferencesDataStoreModule
import ly.david.data.musicbrainz.auth.MusicBrainzDataModule
import ly.david.data.spotify.di.SpotifyDataModule
import ly.david.data.spotify.di.spotifyApiModule
import ly.david.mbjc.di.appDataModule
import ly.david.musicsearch.data.database.databaseDaoModule
import ly.david.musicsearch.data.database.databaseDriverModule
import ly.david.musicsearch.data.database.databaseModule
import ly.david.musicsearch.data.repository.di.repositoryDataModule
import ly.david.musicsearch.domain.DomainModule
import ly.david.musicsearch.domain.InvertedDomainModule
import ly.david.musicsearch.feature.search.di.searchFeatureModule
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.di.historyUiModule
import ly.david.ui.image.di.imageModule
import ly.david.ui.nowplaying.NowPlayingUiModule
import ly.david.ui.settings.SettingsUiModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val androidAppModule = module {
    includes(
        ViewModelsModule().module,
        appDataModule,
        coroutineDispatchersModule,
        coroutinesScopesModule,
        loggingModule,
        musicBrainzAuthModule,
        networkModule,
        spotifyApiModule,
        preferencesDataStoreModule,
        imageModule,
        searchFeatureModule,
        repositoryDataModule,
        InvertedDomainModule().module,
        CoverArtDataModule().module,
        DomainModule().module,
        MusicBrainzDataModule().module,
        SpotifyDataModule().module,
        AuthStoreModule().module,
        CollectionUiModule().module,
        CommonUiModule().module,
        historyUiModule,
        NowPlayingUiModule().module,
        SettingsUiModule().module,

        databaseDriverModule,
        databaseModule,
        databaseDaoModule,
    )
}
