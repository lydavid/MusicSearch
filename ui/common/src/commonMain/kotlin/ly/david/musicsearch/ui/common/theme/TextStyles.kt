package ly.david.musicsearch.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object TextStyles {

    @Composable
    fun getHeaderTextStyle() = MaterialTheme.typography.titleLarge

    @Composable
    fun getCardBodyTextStyle() = MaterialTheme.typography.bodyLarge

    @Composable
    fun getCardBodySubTextStyle() = MaterialTheme.typography.bodySmall
}
