package ly.david.mbjc

import ly.david.musicsearch.core.coroutines.di.coroutinesScopesModule
import ly.david.data.di.musicbrainz.musicBrainzAuthModule
import ly.david.data.test.di.testNetworkModule
import ly.david.mbjc.di.appDataModule
import ly.david.mbjc.di.testCoroutineDispatchersModule
import ly.david.mbjc.di.testDatabaseDriverModule
import ly.david.mbjc.di.testImageModule
import ly.david.mbjc.di.testPreferencesDataStoreModule
import ly.david.musicsearch.android.feature.nowplaying.NowPlayingUiModule
import ly.david.musicsearch.core.logging.loggingModule
import ly.david.musicsearch.core.preferences.di.appPreferencesModule
import ly.david.musicsearch.data.coverart.di.coverArtDataModule
import ly.david.musicsearch.data.database.databaseDaoModule
import ly.david.musicsearch.data.database.databaseModule
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzDataModule
import ly.david.musicsearch.data.repository.di.repositoryDataModule
import ly.david.musicsearch.data.spotify.di.spotifyDataModule
import ly.david.musicsearch.domain.DomainModule
import ly.david.musicsearch.feature.search.di.searchFeatureModule
import ly.david.musicsearch.strings.di.stringsModule
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.di.historyUiModule
import ly.david.ui.settings.SettingsUiModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

// TODO: reorganize this and androidAppModule
/**
 * This should mirror [androidAppModule] with the exception of test modules to fake
 * things like network/database.
 */
val testAndroidAppModule = module {
    includes(
        stringsModule,
        testCoroutineDispatchersModule,
        testNetworkModule,
        testPreferencesDataStoreModule,
        appPreferencesModule,
        testImageModule,
        testDatabaseDriverModule,
        searchFeatureModule,
        spotifyDataModule,
        ViewModelsModule().module,
        appDataModule,
        coroutinesScopesModule,
        loggingModule,
        musicBrainzAuthModule,
        repositoryDataModule,
        coverArtDataModule,
        musicBrainzDataModule,
        CollectionUiModule().module,
        CommonUiModule().module,
        DomainModule().module,
        historyUiModule,
        NowPlayingUiModule().module,
        SettingsUiModule().module,
        databaseModule,
        databaseDaoModule,
    )
}
