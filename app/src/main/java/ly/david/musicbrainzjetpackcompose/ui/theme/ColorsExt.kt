package ly.david.musicbrainzjetpackcompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getListHeaderBackground(): Color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

@Composable
fun getSubtitleColor(): Color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray
