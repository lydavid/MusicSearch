package ly.david.musicsearch.shared.domain.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

internal val coroutinesScopesModule = module {
    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatchers>().default)
    }
}
