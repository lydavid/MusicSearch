package ly.david.mbjc.ui

import ly.david.data.di.network.networkModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class KoinModuleTest {

    @Test
    fun checkKoinModules() {
        networkModule.verify()
    }
}
