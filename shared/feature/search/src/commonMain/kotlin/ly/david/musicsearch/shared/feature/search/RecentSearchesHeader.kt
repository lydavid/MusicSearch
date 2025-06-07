package ly.david.musicsearch.shared.feature.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.DeleteOutline
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun RecentSearchesHeader(
    onDeleteAllHistory: () -> Unit = {},
) {
    val strings = LocalStrings.current

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = strings.recentSearches,
            style = TextStyles.getHeaderTextStyle(),
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onDeleteAllHistory) {
            Icon(
                imageVector = CustomIcons.DeleteOutline,
                contentDescription = strings.clearSearchHistory,
            )
        }
    }
}
