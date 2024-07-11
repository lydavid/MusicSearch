package ly.david.musicsearch.android.app

import ly.david.data.test.di.testApiModule
import ly.david.musicsearch.android.app.di.testPreferencesDataStoreModule
import ly.david.musicsearch.shared.di.sharedModule
import org.koin.dsl.module

val testAndroidAppModule = module {
    includes(
        sharedModule,
        androidAppModule,
        testApiModule,
        testPreferencesDataStoreModule,
    )
}
