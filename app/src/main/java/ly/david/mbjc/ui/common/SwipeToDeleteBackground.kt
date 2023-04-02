package ly.david.mbjc.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp

@Composable
internal fun SwipeToDeleteBackground(
    alignment: Alignment
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Image(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}
