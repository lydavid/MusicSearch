package ly.david.mbjc.ui.artist.stats

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.R
import ly.david.mbjc.data.getDisplayTypes
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupTypeCount
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun ArtistStatsScreen(
    artistId: String,
    lazyListState: LazyListState,
    viewModel: ArtistStatsViewModel = hiltViewModel()
) {
    var totalRemote by rememberSaveable { mutableStateOf(0) }
    var totalLocal by rememberSaveable { mutableStateOf(0) }
    var releaseGroupTypeCounts by rememberSaveable { mutableStateOf(listOf<ReleaseGroupTypeCount>()) }

    var totalRelations by rememberSaveable { mutableStateOf(0) }
    var relationTypeCounts by rememberSaveable { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = totalRemote, key2 = totalLocal) {
        totalRemote = viewModel.getTotalReleaseGroups(artistId)
        totalLocal = viewModel.getNumberOfReleaseGroupsByArtist(artistId)
        releaseGroupTypeCounts = viewModel.getCountOfEachAlbumType(artistId)

        totalRelations = viewModel.getNumberOfRelationsByResource(artistId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(artistId)
    }
    ArtistStatsScreen(
        lazyListState = lazyListState,
        totalRemote = totalRemote,
        totalLocal = totalLocal,
        releaseGroupTypeCounts = releaseGroupTypeCounts,
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts
    )
}

// TODO: list state not saved on tab change
//  would need to hoist its lazy list items
//  Since this screen may have many of these, it may not be worth it.
@Composable
internal fun ArtistStatsScreen(
    lazyListState: LazyListState = LazyListState(),
    totalRemote: Int,
    totalLocal: Int,
    releaseGroupTypeCounts: List<ReleaseGroupTypeCount>,
    totalRelations: Int,
    relationTypeCounts: List<RelationTypeCount>
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
                    text = "Total number of release groups on MusicBrainz network: $totalRemote"
                )
                Text(
                    style = TextStyles.getCardBodyTextStyle(),
                    text = "Total number of release groups in local database: $totalLocal"
                )
            }
        }
        items(releaseGroupTypeCounts) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyles.getCardBodyTextStyle(),
                text = "${it.getDisplayTypes()}: ${it.count}"
            )
        }

        item {
            Spacer(modifier = Modifier.padding(8.dp))
            ListSeparatorHeader(text = stringResource(id = R.string.relationships))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyles.getCardBodyTextStyle(),
                text = "Total number of relationships: $totalRelations"
            )
        }
        items(relationTypeCounts) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyles.getCardBodyTextStyle(),
                text = "${it.linkedResource.displayText}: ${it.count}"
            )
        }

        item {
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            ArtistStatsScreen(
                totalRemote = 280,
                totalLocal = 279,
                releaseGroupTypeCounts = listOf(
                    ReleaseGroupTypeCount(primaryType = "Album", count = 13),
                    ReleaseGroupTypeCount(primaryType = "Album", secondaryTypes = listOf("Compilation", "Demo"), count = 1),
                ),
                totalRelations = 696,
                relationTypeCounts = listOf(
                    RelationTypeCount(linkedResource = MusicBrainzResource.ARTIST, 17),
                    RelationTypeCount(linkedResource = MusicBrainzResource.RECORDING, 397),
                )
            )
        }
    }
}
