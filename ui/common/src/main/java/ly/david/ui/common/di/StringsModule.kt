package ly.david.ui.common.di

import cafe.adriel.lyricist.Lyricist
import ly.david.ui.common.strings.Locales
import ly.david.ui.common.strings.Strings
import org.koin.dsl.module

val stringsModule = module {
    single {
        Lyricist(
            defaultLanguageTag = Locales.EN,
            translations = Strings,
        ).strings
    }
}
