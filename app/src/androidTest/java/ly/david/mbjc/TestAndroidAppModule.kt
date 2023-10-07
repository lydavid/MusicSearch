package ly.david.mbjc

import ly.david.data.coverart.CoverArtDataModule
import ly.david.data.di.auth.AuthStoreModule
import ly.david.data.di.coroutines.coroutinesScopesModule
import ly.david.data.di.logging.loggingModule
import ly.david.data.di.musicbrainz.musicBrainzAuthModule
import ly.david.data.musicbrainz.auth.MusicBrainzDataModule
import ly.david.data.spotify.di.SpotifyDataModule
import ly.david.data.test.di.testNetworkModule
import ly.david.mbjc.di.appDataModule
import ly.david.mbjc.di.testCoroutineDispatchersModule
import ly.david.mbjc.di.testDatabaseDriverModule
import ly.david.mbjc.di.testImageModule
import ly.david.mbjc.di.testPreferencesDataStoreModule
import ly.david.musicsearch.data.database.databaseDaoModule
import ly.david.musicsearch.data.database.databaseModule
import ly.david.musicsearch.data.repository.di.repositoryDataModule
import ly.david.musicsearch.domain.DomainModule
import ly.david.musicsearch.domain.InvertedDomainModule
import ly.david.musicsearch.feature.search.di.searchFeatureModule
import ly.david.musicsearch.strings.di.stringsModule
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.di.historyUiModule
import ly.david.ui.nowplaying.NowPlayingUiModule
import ly.david.ui.settings.SettingsUiModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val testAndroidAppModule = module {
    includes(
        stringsModule,
        testCoroutineDispatchersModule,
        testNetworkModule,
        testPreferencesDataStoreModule,
        testImageModule,
        testDatabaseDriverModule,
        searchFeatureModule,
        ViewModelsModule().module,
        appDataModule,
        coroutinesScopesModule,
        loggingModule,
        musicBrainzAuthModule,
        repositoryDataModule,
        CoverArtDataModule().module,
        DomainModule().module,
        MusicBrainzDataModule().module,
        SpotifyDataModule().module,
        AuthStoreModule().module,
        CollectionUiModule().module,
        CommonUiModule().module,
        InvertedDomainModule().module,
        historyUiModule,
        NowPlayingUiModule().module,
        SettingsUiModule().module,
        databaseModule,
        databaseDaoModule,
    )
}
