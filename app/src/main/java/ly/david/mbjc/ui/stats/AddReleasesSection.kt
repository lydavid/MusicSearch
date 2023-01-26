package ly.david.mbjc.ui.stats

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

// TODO: generalize for event, place, recording
//  these do not show type distribution unlike RG
internal fun LazyListScope.addReleasesSection(
    totalRemote: Int?,
    totalLocal: Int,
) {
    item {
        ListSeparatorHeader(text = stringResource(id = R.string.releases))

        LocalRemoteProgressBar(
            totalRemote = totalRemote,
            totalLocal = totalLocal,
            cachedStringRes = R.string.cached_releases
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
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
                    totalLocal = 7
                )
            }
        }
    }
}
