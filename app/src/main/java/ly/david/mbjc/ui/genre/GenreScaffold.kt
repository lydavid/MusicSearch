package ly.david.mbjc.ui.genre

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.data.common.lookupInBrowser
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.fullscreen.FullScreenContent
import ly.david.ui.common.topappbar.ScrollableTopAppBar
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles
import org.koin.androidx.compose.koinViewModel

// TODO: genre browsing isn't supported by API
//  lookup only returns its name so there's not much for us to display ATM
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GenreScaffold(
    genreId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    viewModel: GenreScaffoldViewModel = koinViewModel(),
) {
    val resource = MusicBrainzEntity.GENRE
    val strings = LocalStrings.current
    val context = LocalContext.current
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val genre by viewModel.genre.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = genreId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = forceRefresh) {
        viewModel.onSelectedTabChange(
            genreId = genreId
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                entity = resource,
                title = title,
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        DetailsWithErrorHandling(
            showError = showError,
            onRetryClick = { forceRefresh = true },
            scaffoldModel = genre
        ) {
            FullScreenContent(modifier = Modifier.padding(innerPadding)) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = "Genre lookup is currently not supported in this app.",
                    style = TextStyles.getCardBodyTextStyle()
                )
                Button(onClick = {
                    context.lookupInBrowser(resource, genreId)
                }) {
                    Text(strings.openInBrowser)
                }
            }
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        GenreScaffold(
            genreId = "911c7bbb-172d-4df8-9478-dbff4296e791",
            titleWithDisambiguation = "pop",
        )
    }
}
