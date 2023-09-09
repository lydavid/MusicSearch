package ly.david.data.di.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val coroutinesScopesModule = module {
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }
}
