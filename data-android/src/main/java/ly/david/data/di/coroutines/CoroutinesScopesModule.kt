package ly.david.data.di.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutinesScopesModule = module {
    single {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(MusicSearchDispatchers.Default)))
    }
}
