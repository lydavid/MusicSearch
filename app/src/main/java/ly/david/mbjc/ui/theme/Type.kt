package ly.david.mbjc.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

internal object TextStyles {

    @Composable
    fun getCardTitleTextStyle() = MaterialTheme.typography.titleLarge

    @Composable
    fun getCardBodyTextStyle() = MaterialTheme.typography.bodyLarge

    @Composable
    fun getCardBodySubTextStyle() = MaterialTheme.typography.bodySmall
}
