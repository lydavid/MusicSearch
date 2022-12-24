package ly.david.mbjc.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// TODO: now that we support user-selected theme, we can't use  isSystemInDarkTheme() throughout app
@Composable
internal fun getSubBackgroundColor(): Color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

@Composable
internal fun getAlertBackgroundColor(): Color = if (isSystemInDarkTheme()) Color.DarkGray else Color.White

@Composable
internal fun getSubTextColor(): Color = if (isSystemInDarkTheme()) Color.White else Color.Gray
