package ly.david.mbjc

import ly.david.data.test.di.testApiModule
import ly.david.mbjc.di.androidAppModule
import ly.david.mbjc.di.testCoroutineDispatchersModule
import ly.david.mbjc.di.testDatabaseDriverModule
import ly.david.mbjc.di.testImageModule
import ly.david.mbjc.di.testPreferencesDataStoreModule
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.musicsearch.shared.feature.details.ViewModelsModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val testAndroidAppModule = module {
    includes(
        sharedModule,
        androidAppModule,
        ViewModelsModule().module,
        testCoroutineDispatchersModule,
        testApiModule,
        testPreferencesDataStoreModule,
        testImageModule,
        testDatabaseDriverModule,
    )
}
