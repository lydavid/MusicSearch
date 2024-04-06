package ly.david.musicsearch.strings

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalStrings: ProvidableCompositionLocal<AppStrings> = staticCompositionLocalOf { EnStrings }