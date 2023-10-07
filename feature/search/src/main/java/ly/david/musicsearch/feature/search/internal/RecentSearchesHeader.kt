package ly.david.musicsearch.feature.search.internal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun RecentSearchesHeader(
    isListEmpty: Boolean = false,
    onDeleteAllHistory: () -> Unit = {},
) {
    val strings = LocalStrings.current

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = strings.recentSearches,
            style = TextStyles.getHeaderTextStyle(),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.weight(1f))

        if (!isListEmpty) {
            IconButton(onClick = onDeleteAllHistory) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = strings.clearSearchHistory,
                )
            }
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewRecentSearchesHeader() {
    PreviewTheme {
        Surface {
            RecentSearchesHeader()
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewRecentSearchesHeaderEmpty() {
    PreviewTheme {
        Surface {
            RecentSearchesHeader(isListEmpty = true)
        }
    }
}
