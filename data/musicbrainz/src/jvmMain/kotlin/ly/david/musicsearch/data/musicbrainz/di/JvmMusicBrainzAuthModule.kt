package ly.david.musicsearch.data.musicbrainz.di

import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.ServiceBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val musicBrainzAuthModule: Module = module {
    single {
        val service = ServiceBuilder("key")
            .apiSecret("secret")
            .build(TwitterApi.instance())
    }
}
