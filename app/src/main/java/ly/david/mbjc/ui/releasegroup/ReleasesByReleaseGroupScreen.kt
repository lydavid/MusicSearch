package ly.david.mbjc.ui.releasegroup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.UiState
import ly.david.mbjc.ui.common.toDate
import ly.david.mbjc.ui.release.ReleaseCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleasesByReleaseGroupScreen(
    modifier: Modifier,
    releaseGroupId: String,
    onReleaseClick: (String) -> Unit = {},
    viewModel: ReleasesByReleaseGroupViewModel = hiltViewModel()
) {
    val uiState by produceState(initialValue = UiState(isLoading = true)) {
        value = UiState(response = viewModel.getReleasesByReleaseGroup(releaseGroupId))
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { releases ->

                LazyColumn(
                    modifier = modifier
                ) {
                    val grouped = releases.groupBy { it.status ?: "(No status)" }
                    grouped.forEach { (status, releasesWithStatus) ->
                        stickyHeader {
                            ListSeparatorHeader(text = status)
                        }
                        items(releasesWithStatus.sortedBy {
                            it.date?.toDate()
                        }) { release ->
                            // TODO: sort by date ascending
                            ReleaseCard(release = release) {
                                onReleaseClick(it.id)
                            }
                        }
                    }

                }
            }

        }
        uiState.isLoading -> {
            FullScreenLoadingIndicator()
        }
        else -> {
            Text(text = "error...")
        }
    }
}
