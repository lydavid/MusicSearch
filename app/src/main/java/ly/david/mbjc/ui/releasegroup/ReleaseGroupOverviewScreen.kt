package ly.david.mbjc.ui.releasegroup

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.data.UiReleaseGroup
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

                onTitleUpdate(
                    releaseGroup.getTitleWithDisambiguation(),
                    "Release Group by ${releaseGroup.artistCredits}"
                )

                ReleaseGroupDetails(releaseGroup)
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

// TODO: Is there enough details about a release group to warrant its own tab?
//  The releases in it are its main content, so maybe put that in the first tab like on web
//  would have to main additional api calls, but we can make use of caching
@Composable
fun ReleaseGroupDetails(releaseGroup: UiReleaseGroup) {
    Column {
        Text(text = releaseGroup.primaryType.orEmpty())
        Text(text = releaseGroup.secondaryTypes.toString())
    }
}
