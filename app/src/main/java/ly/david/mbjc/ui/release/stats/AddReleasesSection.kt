package ly.david.mbjc.ui.release.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

internal fun LazyListScope.addReleasesSection(
    totalRemote: Int,
    totalLocal: Int,
) {
    item {
        ListSeparatorHeader(text = stringResource(id = R.string.releases))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                style = TextStyles.getCardBodyTextStyle(),
                text = "Releases on MusicBrainz network: $totalRemote"
            )
            Text(
                style = TextStyles.getCardBodyTextStyle(),
                text = "Releases in local database: $totalLocal"
            )
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addReleasesSection(
                    totalRemote = 10,
                    totalLocal = 9
                )
            }
        }
    }
}
