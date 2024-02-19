package ly.david.ui.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupTypeCount
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

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
                            count = 1,
                        ),
                    ),
                )
            }
        }
    }
}
