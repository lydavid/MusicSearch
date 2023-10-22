package ly.david.mbjc

import ly.david.data.test.di.testApiModule
import ly.david.mbjc.di.appDataModule
import ly.david.mbjc.di.testCoroutineDispatchersModule
import ly.david.mbjc.di.testDatabaseDriverModule
import ly.david.mbjc.di.testImageModule
import ly.david.mbjc.di.testPreferencesDataStoreModule
import ly.david.musicsearch.shared.di.coreModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val testAndroidAppModule = module {
    includes(
        coreModule,
        appDataModule,
        ViewModelsModule().module,
        testCoroutineDispatchersModule,
        testApiModule,
        testPreferencesDataStoreModule,
        testImageModule,
        testDatabaseDriverModule,
    )
}
