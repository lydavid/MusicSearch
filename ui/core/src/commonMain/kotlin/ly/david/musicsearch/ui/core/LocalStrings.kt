package ly.david.musicsearch.ui.core

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.shared.strings.EnStrings

val LocalStrings: ProvidableCompositionLocal<AppStrings> = staticCompositionLocalOf { EnStrings }
