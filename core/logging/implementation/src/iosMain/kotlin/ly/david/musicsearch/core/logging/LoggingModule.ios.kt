package ly.david.musicsearch.core.logging

import org.koin.core.module.Module
import org.koin.dsl.module

actual val loggingModule: Module = module {
    single<Logger> {
        object : Logger {
            override fun d(text: String) {
                println(text)
            }

            override fun e(exception: Exception) {
                println(exception)
            }
        }
    }
}
