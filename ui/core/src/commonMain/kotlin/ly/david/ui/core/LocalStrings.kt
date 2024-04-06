package ly.david.ui.core

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ly.david.musicsearch.strings.AppStrings
import ly.david.musicsearch.strings.EnStrings

val LocalStrings: ProvidableCompositionLocal<AppStrings> = staticCompositionLocalOf { EnStrings }
