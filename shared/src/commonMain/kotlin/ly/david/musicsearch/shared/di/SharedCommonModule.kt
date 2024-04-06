package ly.david.musicsearch.shared.di

import com.slack.circuit.foundation.Circuit
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
import ly.david.musicsearch.domain.DomainModule
import ly.david.musicsearch.shared.feature.stats.statsFeatureModule
import ly.david.musicsearch.shared.feature.collections.collectionsFeatureModule
import ly.david.musicsearch.shared.feature.details.detailsFeatureModule
import ly.david.musicsearch.shared.feature.search.searchFeatureModule
import ly.david.musicsearch.shared.feature.history.historyFeatureModule
import ly.david.musicsearch.shared.feature.licenses.licensesFeatureModule
import ly.david.musicsearch.shared.feature.settings.settingsFeatureModule
import ly.david.musicsearch.strings.di.stringsModule
import ly.david.ui.common.commonUiModule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

val circuitModule = module {
    includes(
        searchFeatureModule,
        historyFeatureModule,
        collectionsFeatureModule,
        settingsFeatureModule,
        licensesFeatureModule,
        detailsFeatureModule,
        statsFeatureModule,
    )
    single {
        Circuit.Builder()
            .addPresenterFactories(getAll())
            .addUiFactories(getAll())
            .build()
    }
}

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
        commonUiModule,
        circuitModule,
        DomainModule().module,
    )
}
