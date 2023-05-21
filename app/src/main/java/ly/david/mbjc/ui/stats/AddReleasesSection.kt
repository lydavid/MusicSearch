package ly.david.mbjc.ui.stats

import androidx.annotation.StringRes
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
import ly.david.ui.common.theme.PreviewTheme

internal fun LazyListScope.addResourcesStatsSection(
    totalRemote: Int?,
    totalLocal: Int,
    @StringRes headerRes: Int,
    @StringRes cachedLocalOfRemoteRes: Int
) {
    item {
        ListSeparatorHeader(text = stringResource(id = headerRes))

        LocalRemoteProgressBar(
            totalRemote = totalRemote,
            totalLocal = totalLocal,
            cachedLocalOfRemoteRes = cachedLocalOfRemoteRes
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
                addResourcesStatsSection(
                    totalRemote = 10,
                    totalLocal = 7,
                    headerRes = R.string.releases,
                    cachedLocalOfRemoteRes = R.string.cached_releases
                )
            }
        }
    }
}
