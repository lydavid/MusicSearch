package ly.david.musicsearch.core.coroutines.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import org.koin.dsl.module

val coroutinesScopesModule = module {
    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatchers>().default)
    }
}
