package ly.david.mbjc.ui.artist.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.getDisplayTypes
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.addSpacer
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.stats.addRelationshipsSection
import ly.david.mbjc.ui.stats.addReleasesSection
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun ArtistStatsScreen(
    artistId: String,
    lazyListState: LazyListState,
    viewModel: ArtistStatsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val artistStats by remember { viewModel.getStats(artistId, coroutineScope) }.collectAsState()

    ArtistStatsScreen(
        lazyListState = lazyListState,
        stats = artistStats
    )
}

// TODO: [low] list state not saved on tab change
//  would need to hoist its lazy list items
//  Since this screen may have many of these, it may not be worth it.
@Composable
private fun ArtistStatsScreen(
    lazyListState: LazyListState = LazyListState(),
    stats: ArtistStats
) {
    LazyColumn(
        state = lazyListState
    ) {
        item {
            ListSeparatorHeader(text = stringResource(id = R.string.release_groups))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Text(
                    style = TextStyles.getCardBodyTextStyle(),
                    text = "Cached release groups\n ${stats.totalLocalReleaseGroups} / ${stats.totalRemoteReleaseGroups}"
                )
            }
        }
        items(stats.releaseGroupTypeCounts) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
            ) {
                Text(
                    style = TextStyles.getCardBodyTextStyle(),
                    text = "${it.getDisplayTypes()}: ${it.count}"
                )

                if (stats.releaseGroupTypeCounts.isNotEmpty()) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth(),
                        progress = it.count / stats.releaseGroupTypeCounts.sumOf { it.count }.toFloat(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.Transparent
                    )
                }
            }

        }

        addSpacer()

        addReleasesSection(
            totalRemote = stats.totalRemoteReleases,
            totalLocal = stats.totalLocalReleases
        )

        addRelationshipsSection(
            totalRelations = stats.totalRelations,
            relationTypeCounts = stats.relationTypeCounts
        )
    }
}

// region Previews
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            ArtistStatsScreen(
                stats = ArtistStats(
                    totalRemoteReleaseGroups = 280,
                    totalLocalReleaseGroups = 279,
                    releaseGroupTypeCounts = listOf(
                        ReleaseGroupTypeCount(primaryType = "Album", count = 13),
                        ReleaseGroupTypeCount(
                            primaryType = "Album",
                            secondaryTypes = listOf("Compilation", "Demo"),
                            count = 1
                        ),
                    ),
                    totalRelations = 696,
                    relationTypeCounts = listOf(
                        RelationTypeCount(linkedResource = MusicBrainzResource.ARTIST, 17),
                        RelationTypeCount(linkedResource = MusicBrainzResource.RECORDING, 397),
                    ),
                    totalRemoteReleases = 20,
                    totalLocalReleases = 15
                )
            )
        }
    }
}
// endregion
