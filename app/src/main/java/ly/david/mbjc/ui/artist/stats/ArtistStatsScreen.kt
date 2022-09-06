package ly.david.mbjc.ui.artist.stats

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ly.david.mbjc.data.getDisplayTypes
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupTypeCount

@Composable
internal fun ArtistStatsScreen(
    artistId: String,
    viewModel: ArtistStatsViewModel = hiltViewModel()
) {
    var totalRemote by rememberSaveable { mutableStateOf(0) }
    var totalLocal by rememberSaveable { mutableStateOf(0) }
    var releaseGroupTypeCounts by rememberSaveable { mutableStateOf(listOf<ReleaseGroupTypeCount>()) }

    LaunchedEffect(key1 = totalRemote, key2 = totalLocal) {
        totalRemote = viewModel.getTotalReleaseGroups(artistId)
        totalLocal = viewModel.getNumberOfReleaseGroupsByArtist(artistId)
        releaseGroupTypeCounts = viewModel.getCountOfEachAlbumType(artistId)
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        item {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "Release groups on MusicBrainz network: $totalRemote"
            )
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "Release groups in local database: $totalLocal"
            )
        }
        items(releaseGroupTypeCounts) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "${it.getDisplayTypes()}: ${it.count}"
            )
        }
    }
}
