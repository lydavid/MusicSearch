package ly.david.data.di.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.musicsearch.data.core.CoroutineDispatchers
import org.koin.dsl.module

val coroutinesScopesModule = module {
    single {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatchers>().default)
    }
}
