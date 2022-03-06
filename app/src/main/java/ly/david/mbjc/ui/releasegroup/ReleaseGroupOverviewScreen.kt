package ly.david.mbjc.ui.releasegroup

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.data.getTitleWithDisambiguation
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.UiState

@Composable
fun ReleaseGroupOverviewScreen(
    releaseGroupId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit,
    viewModel: ReleaseGroupOverviewViewModel = hiltViewModel()
) {

    val uiState by produceState(initialValue = UiState(isLoading = true)) {
        value = UiState(response = viewModel.lookupReleaseGroup(releaseGroupId))
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { releaseGroup ->

                // TODO: UI should receive a domain model
                onTitleUpdate(
                    releaseGroup.getTitleWithDisambiguation(),
                    // TODO: should include all credits, with joinphrase
                    "Release Group by ${releaseGroup.artistCredits?.map { it.name + it.joinPhrase }}"
                )
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
