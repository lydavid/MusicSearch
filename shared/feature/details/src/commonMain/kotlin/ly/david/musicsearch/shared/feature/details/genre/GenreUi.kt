package ly.david.musicsearch.shared.feature.details.genre

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
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.fullscreen.FullScreenContent
import ly.david.ui.common.topappbar.ScrollableTopAppBar
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun GenreUi(
    state: GenreUiState,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    GenreUi(
        title = state.title,
        entityId = entityId,
        isError = state.isError,
        genre = state.genre,
        modifier = modifier,
        onBack = {
            eventSink(GenreUiEvent.NavigateUp)
        },
        onRetryClick = { eventSink(GenreUiEvent.ForceRefresh) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GenreUi(
    title: String,
    entityId: String,
    genre: GenreMusicBrainzModel?,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onBack: () -> Unit = {},
    onRetryClick: () -> Unit = {},
) {
    val entity = MusicBrainzEntity.GENRE

    val strings = LocalStrings.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                entity = entity,
                title = title,
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        DetailsWithErrorHandling(
            showError = isError,
            onRefresh = onRetryClick,
            scaffoldModel = genre,
        ) {
            FullScreenContent(modifier = Modifier.padding(innerPadding)) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = "Genre lookup is currently not supported in this app.",
                    style = TextStyles.getCardBodyTextStyle(),
                )

                Button(
                    onClick = {
                        uriHandler.openUri("$MUSIC_BRAINZ_BASE_URL/${entity.resourceUri}/$entityId")
                    },
                ) {
                    Text(strings.openInBrowser)
                }
            }
        }
    }
}
