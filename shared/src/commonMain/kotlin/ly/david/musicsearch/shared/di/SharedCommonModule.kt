package ly.david.musicsearch.shared.di

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
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzAuthModule
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzDataModule
import ly.david.musicsearch.data.repository.di.repositoryDataModule
import ly.david.musicsearch.data.spotify.di.spotifyApiModule
import ly.david.musicsearch.data.spotify.di.spotifyDataModule
import ly.david.musicsearch.feature.search.di.searchFeatureModule
import ly.david.musicsearch.strings.di.stringsModule
import org.koin.core.module.Module
import org.koin.dsl.module

// By putting some of these into nested includes, we will crash
// Is it due to some of the modules depending on others
// and when they are not at the same nested level, we cannot fulfill them?
val sharedModule: Module = module {
    includes(
        platformModule,
        coroutinesScopesModule,
        loggingModule,
        musicBrainzAuthModule,
        HttpClientModule,
        spotifyDataModule,
        appPreferencesModule,
        repositoryDataModule,
        coverArtDataModule,
        musicBrainzDataModule,
        databaseModule,
        databaseDaoModule,
        stringsModule,
        coroutineDispatchersModule,
        coverArtApiModule,
        spotifyApiModule,
        databaseDriverModule,
        musicBrainzApiModule,
        preferencesDataStoreModule,
        searchFeatureModule,
    )
}
