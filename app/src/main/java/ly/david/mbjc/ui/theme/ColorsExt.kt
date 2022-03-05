package ly.david.mbjc.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getSubBackgroundColor(): Color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

@Composable
fun getAlertBackgroundColor(): Color = if (isSystemInDarkTheme()) Color.DarkGray else Color.White

@Composable
fun getSubTextColor(): Color = if (isSystemInDarkTheme()) Color.White else Color.Gray
