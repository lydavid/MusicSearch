package ly.david.mbjc

import ly.david.data.di.musicbrainz.musicBrainzAuthModule
import ly.david.mbjc.di.appDataModule
import ly.david.musicsearch.android.feature.nowplaying.NowPlayingUiModule
import ly.david.musicsearch.core.coroutines.di.coroutineDispatchersModule
import ly.david.musicsearch.core.coroutines.di.coroutinesScopesModule
import ly.david.musicsearch.core.logging.loggingModule
import ly.david.musicsearch.core.preferences.di.appPreferencesModule
import ly.david.musicsearch.core.preferences.di.preferencesDataStoreModule
import ly.david.musicsearch.data.common.network.di.HttpClientModule
import ly.david.musicsearch.data.coverart.di.coverArtApiModule
import ly.david.musicsearch.data.coverart.di.coverArtDataModule
import ly.david.musicsearch.data.database.databaseDaoModule
import ly.david.musicsearch.data.database.databaseDriverModule
import ly.david.musicsearch.data.database.databaseModule
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzApiModule
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzDataModule
import ly.david.musicsearch.data.repository.di.repositoryDataModule
import ly.david.musicsearch.data.spotify.di.spotifyApiModule
import ly.david.musicsearch.data.spotify.di.spotifyDataModule
import ly.david.musicsearch.domain.DomainModule
import ly.david.musicsearch.feature.search.di.searchFeatureModule
import ly.david.musicsearch.ui.image.di.imageModule
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.di.historyUiModule
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
        HttpClientModule,
        spotifyDataModule,
        spotifyApiModule,
        preferencesDataStoreModule,
        appPreferencesModule,
        imageModule,
        searchFeatureModule,
        repositoryDataModule,
        DomainModule().module,
        coverArtDataModule,
        coverArtApiModule,
        musicBrainzDataModule,
        musicBrainzApiModule,
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
