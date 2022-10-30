package ly.david.mbjc.ui.release.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.relation.stats.addRelationshipsSection
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun ReleaseStatsScreen(
    releaseId: String,
    releaseUiModel: ReleaseUiModel?,
    viewModel: ReleaseStatsViewModel = hiltViewModel()
) {
//    var totalRemote by rememberSaveable { mutableStateOf(0) }
//    var totalLocal by rememberSaveable { mutableStateOf(0) }
//
    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by rememberSaveable { mutableStateOf(listOf<RelationTypeCount>()) }
//
    LaunchedEffect(key1 = Unit) {
//        totalRemote = viewModel.getTotalReleases(releaseId)
//        totalLocal = viewModel.getNumberOfReleasesInReleaseGroup(releaseId)
//
        totalRelations = viewModel.getNumberOfRelationsByResource(releaseId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(releaseId)
    }

    // TODO: since these info exists from release group, we should put them in details instead
    //  also if we deeplink into a release, we won't have them yet, we could check if they exists
    //  and if not, then insert them into room
    ReleaseStatsScreen(
//        totalRemote = totalRemote,
//        totalLocal = totalLocal,
        formats = releaseUiModel?.formats,
        tracks = releaseUiModel?.tracks,
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts
    )
}

@Composable
internal fun ReleaseStatsScreen(
//    totalRemote: Int,
//    totalLocal: Int,
    formats: String?,
    tracks: String?,
    totalRelations: Int?,
    relationTypeCounts: List<RelationTypeCount>
) {
    LazyColumn {
//        item {
//            if (formats != null) {
//                ListSeparatorHeader(text = stringResource(id = R.string.format))
//                Text(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    style = TextStyles.getCardBodyTextStyle(),
//                    text = formats
//                )
//                Spacer(modifier = Modifier.padding(top = 16.dp))
//            }
//
//            if (tracks != null) {
//                ListSeparatorHeader(text = stringResource(id = R.string.tracks))
//                Text(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    style = TextStyles.getCardBodyTextStyle(),
//                    text = tracks
//                )
//            }
//        }
//        addSpacer()
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
            ReleaseStatsScreen(
                formats = "2×CD + Blu-ray",
                tracks = "15 + 8 + 24",
                totalRelations = 3,
                relationTypeCounts = listOf(
                    RelationTypeCount(MusicBrainzResource.URL, 3)
                )
            )
        }
    }
}
