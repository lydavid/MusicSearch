package ly.david.mbjc.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.data.getDisplayTypes
import ly.david.data.room.releasegroup.ReleaseGroupTypeCount
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.R
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

internal fun LazyListScope.addReleaseGroupsSection(
    totalRemote: Int?,
    totalLocal: Int,
    releaseGroupTypeCounts: List<ReleaseGroupTypeCount>
) {
    item {
        ListSeparatorHeader(text = stringResource(id = R.string.release_groups))

        LocalRemoteProgressBar(
            totalRemote = totalRemote,
            totalLocal = totalLocal,
            cachedLocalOfRemoteRes = R.string.cached_release_groups
        )
    }
    items(releaseGroupTypeCounts) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
        ) {
            Text(
                style = TextStyles.getCardBodySubTextStyle(),
                text = "${it.getDisplayTypes()}: ${it.count}"
            )

            if (releaseGroupTypeCounts.isNotEmpty()) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(),
                    progress = it.count / releaseGroupTypeCounts.sumOf { it.count }.toFloat(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.Transparent
                )
            }
        }
    }
    item {
        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addReleaseGroupsSection(
                    totalRemote = 280,
                    totalLocal = 281,
                    releaseGroupTypeCounts = listOf(
                        ReleaseGroupTypeCount(primaryType = "Album", count = 13),
                        ReleaseGroupTypeCount(
                            primaryType = "Album",
                            secondaryTypes = listOf("Compilation", "Demo"),
                            count = 1
                        ),
                    )
                )
            }
        }
    }
}
