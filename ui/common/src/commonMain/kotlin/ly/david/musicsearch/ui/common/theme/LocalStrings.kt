package ly.david.musicsearch.ui.common.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.shared.strings.EnStrings

@Suppress("CompositionLocalAllowlist")
val LocalStrings: ProvidableCompositionLocal<AppStrings> = staticCompositionLocalOf { EnStrings }
