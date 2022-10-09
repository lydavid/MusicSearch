package ly.david.mbjc.ui.releasegroup.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.R
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.relation.stats.addRelationshipsSection
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun ReleaseGroupStatsScreen(
    releaseGroupId: String,
    viewModel: ReleaseGroupStatsViewModel = hiltViewModel()
) {
    var totalRemote by rememberSaveable { mutableStateOf(0) }
    var totalLocal by rememberSaveable { mutableStateOf(0) }

    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by rememberSaveable { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = totalRemote, key2 = totalLocal) {
        totalRemote = viewModel.getTotalReleases(releaseGroupId)
        totalLocal = viewModel.getNumberOfReleasesInReleaseGroup(releaseGroupId)

        totalRelations = viewModel.getNumberOfRelationsByResource(releaseGroupId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(releaseGroupId)
    }

    ReleaseGroupStatsScreen(
        totalRemote = totalRemote,
        totalLocal = totalLocal,
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts
    )
}

@Composable
internal fun ReleaseGroupStatsScreen(
    totalRemote: Int,
    totalLocal: Int,
    totalRelations: Int?,
    relationTypeCounts: List<RelationTypeCount>
) {
    LazyColumn {
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

        addRelationshipsSection(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts
        )
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            ReleaseGroupStatsScreen(
                totalRemote = 1,
                totalLocal = 2,
                totalRelations = 3,
                relationTypeCounts = listOf(
                    RelationTypeCount(MusicBrainzResource.URL, 3)
                )
            )
        }
    }
}
