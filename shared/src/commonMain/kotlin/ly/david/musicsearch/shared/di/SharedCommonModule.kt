package ly.david.musicsearch.shared.di

import com.slack.circuit.foundation.Circuit
import ly.david.musicsearch.core.logging.loggingModule
import ly.david.musicsearch.core.preferences.di.appPreferencesModule
import ly.david.musicsearch.core.preferences.di.preferencesDataStoreModule
import ly.david.musicsearch.data.common.network.di.HttpClientModule
import ly.david.musicsearch.data.coverart.di.coverArtModule
import ly.david.musicsearch.data.database.databaseDaoModule
import ly.david.musicsearch.data.database.databaseModule
import ly.david.musicsearch.data.database.databasePlatformModule
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzApiModule
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzAuthModule
import ly.david.musicsearch.data.musicbrainz.di.musicBrainzDataModule
import ly.david.musicsearch.data.repository.di.repositoryDataModule
import ly.david.musicsearch.data.spotify.di.spotifyApiModule
import ly.david.musicsearch.data.spotify.di.spotifyDataModule
import ly.david.musicsearch.data.wikimedia.di.wikimediaModule
import ly.david.musicsearch.share.feature.database.databaseFeatureModule
import ly.david.musicsearch.shared.domain.domainModule
import ly.david.musicsearch.shared.feature.collections.collectionsFeatureModule
import ly.david.musicsearch.shared.feature.details.detailsFeatureModule
import ly.david.musicsearch.shared.feature.graph.graphFeatureModule
import ly.david.musicsearch.shared.feature.history.historyFeatureModule
import ly.david.musicsearch.shared.feature.images.imagesFeatureModule
import ly.david.musicsearch.shared.feature.licenses.licensesFeatureModule
import ly.david.musicsearch.shared.feature.search.searchFeatureModule
import ly.david.musicsearch.shared.feature.settings.settingsFeatureModule
import ly.david.musicsearch.shared.feature.stats.statsFeatureModule
import ly.david.musicsearch.shared.strings.di.stringsModule
import ly.david.musicsearch.ui.common.commonUiModule
import org.koin.core.module.Module
import org.koin.dsl.module

val circuitModule = module {
    includes(
        searchFeatureModule,
        graphFeatureModule,
        databaseFeatureModule,
        historyFeatureModule,
        collectionsFeatureModule,
        settingsFeatureModule,
        licensesFeatureModule,
        detailsFeatureModule,
        statsFeatureModule,
        imagesFeatureModule,
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
        loggingModule,
        musicBrainzAuthModule,
        HttpClientModule,
        spotifyDataModule,
        appPreferencesModule,
        repositoryDataModule,
        musicBrainzDataModule,
        databaseModule,
        databaseDaoModule,
        stringsModule,
        coverArtModule,
        spotifyApiModule,
        wikimediaModule,
        databasePlatformModule,
        musicBrainzApiModule,
        preferencesDataStoreModule,
        commonUiModule,
        domainModule,
        circuitModule,
    )
}
