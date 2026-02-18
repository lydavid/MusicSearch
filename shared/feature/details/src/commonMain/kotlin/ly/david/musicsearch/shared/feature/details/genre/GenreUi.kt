package ly.david.musicsearch.shared.feature.details.genre

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.musicsearch.ui.common.fullscreen.FullScreenContent
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.openInBrowser
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GenreUi(
    state: GenreUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    val entity = MusicBrainzEntityType.GENRE

    val uriHandler = LocalUriHandler.current

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                entity = entity,
                title = state.title,
                onBack = { eventSink(GenreUiEvent.NavigateUp) },
            )
        },
    ) { innerPadding ->
        DetailsWithErrorHandling(
            modifier = Modifier.padding(innerPadding),
            handledException = state.handledException,
            onRefresh = { eventSink(GenreUiEvent.ForceRefresh) },
            detailsModel = state.genre,
        ) {
            FullScreenContent {
                Text(
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = "Genre lookup is currently not supported in this app.",
                    style = TextStyles.getCardBodyTextStyle(),
                )

                Button(
                    onClick = {
                        uriHandler.openUri(state.url)
                    },
                ) {
                    Text(stringResource(Res.string.openInBrowser))
                }
            }
        }
    }
}
