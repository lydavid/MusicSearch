package ly.david.mbjc.ui.releasegroup

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.mbjc.data.Release
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.StickyHeader
import ly.david.mbjc.ui.common.UiState
import ly.david.mbjc.ui.common.toDate
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.mbjc.ui.theme.getSubTextColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleasesByReleaseGroupScreen(
    modifier: Modifier,
    releaseGroupId: String,
    onReleaseClick: (String) -> Unit = {},
    viewModel: ReleasesByReleaseGroupViewModel = viewModel()
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
                            StickyHeader(text = status)
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

// TODO: needs: format/tracks/country/
@Composable
private fun ReleaseCard(
    release: Release,
    onClick: (Release) -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(release) },
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(2f),
            ) {
                Row {
                    Text(
                        text = release.name,
                        style = MaterialTheme.typography.h6,
                    )
                }

                val disambiguation = release.disambiguation
                if (disambiguation.isNotEmpty()) {
                    Row {
                        Text(
                            text = "($disambiguation)",
                            color = getSubTextColor(),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }

            val date = release.date
            if (!date.isNullOrEmpty()) {
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(
                    // TODO: Is there a way to ensure a composable that comes after another
                    //  is given enough space to fit its text?
                    modifier = Modifier.weight(1f),
                    text = date,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

private val testRelease = Release(
    id = "1",
    name = "Release title that is long and wraps",
    disambiguation = "Disambiguation text that is also long",
    date = "2021-09-08",
    country = "JP"
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ReleaseCard(testRelease)
        }
    }
}

private val testRelease2 = Release(
    id = "1",
    name = "Release title",
    disambiguation = "Disambiguation text",
    country = "JP"
)

@Preview
@Composable
internal fun ReleaseCardWithoutYearPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ReleaseCard(testRelease2)
        }
    }
}
