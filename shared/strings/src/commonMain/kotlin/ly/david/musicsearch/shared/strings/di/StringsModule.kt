package ly.david.musicsearch.shared.strings.di

import cafe.adriel.lyricist.Lyricist
import ly.david.musicsearch.shared.strings.Locales
import ly.david.musicsearch.shared.strings.Strings
import org.koin.dsl.module

val stringsModule = module {
    single {
        Lyricist(
            defaultLanguageTag = Locales.EN,
            translations = Strings,
        ).strings
    }
}
