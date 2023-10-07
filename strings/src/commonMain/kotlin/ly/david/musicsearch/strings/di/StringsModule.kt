package ly.david.musicsearch.strings.di

import cafe.adriel.lyricist.Lyricist
import ly.david.musicsearch.strings.Locales
import ly.david.musicsearch.strings.Strings
import org.koin.dsl.module

val stringsModule = module {
    single {
        Lyricist(
            defaultLanguageTag = Locales.EN,
            translations = Strings,
        ).strings
    }
}
