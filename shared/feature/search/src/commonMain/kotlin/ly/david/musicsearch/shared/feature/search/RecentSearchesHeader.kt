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
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.clearSearchHistory
import musicsearch.ui.common.generated.resources.recentSearches
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RecentSearchesHeader(
    onDeleteAllHistory: () -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.recentSearches),
            style = TextStyles.getHeaderTextStyle(),
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onDeleteAllHistory) {
            Icon(
                imageVector = CustomIcons.DeleteOutline,
                contentDescription = stringResource(Res.string.clearSearchHistory),
            )
        }
    }
}
