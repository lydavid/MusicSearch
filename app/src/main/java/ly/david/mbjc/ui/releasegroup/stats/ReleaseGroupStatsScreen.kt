package ly.david.mbjc.ui.releasegroup.stats

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ReleaseGroupStatsScreen(
    releaseGroupId: String,
    viewModel: ReleaseGroupStatsViewModel = hiltViewModel()
) {
    var totalRemote by rememberSaveable { mutableStateOf(0) }
    var totalLocal by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(key1 = totalRemote, key2 = totalLocal) {
        totalRemote = viewModel.getTotalReleases(releaseGroupId)
        totalLocal = viewModel.getNumberOfReleasesInReleaseGroup(releaseGroupId)
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        item {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "Releases on MusicBrainz network: $totalRemote"
            )
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "Releases in local database: $totalLocal"
            )
        }
    }
}
