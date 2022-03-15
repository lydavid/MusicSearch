package ly.david.mbjc.ui.artist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
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
import ly.david.mbjc.data.persistence.ReleaseGroupTypeCount

@Composable
fun ArtistStatsScreen(
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

    LazyColumn {
        item {
            Text(text = "Release groups on MusicBrainz network: $totalRemote")
            Text(text = "Release groups in local database: $totalLocal")

            if (releaseGroupTypeCounts.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Release groups types in local database"
                )
            }
        }
        items(releaseGroupTypeCounts) {
            Text(text = "${it.getDisplayTypes()}: ${it.count}")
        }
    }
}
