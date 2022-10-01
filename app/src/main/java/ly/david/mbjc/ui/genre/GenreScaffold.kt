package ly.david.mbjc.ui.genre

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.R
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.fullscreen.FullScreenContent
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

// TODO: genre browsing isn't supported by API
//  lookup only returns its name so there's not much for us to display ATM
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GenreScaffold(
    genreId: String,
    title: String? = null,
    onBack: () -> Unit,
) {

    val resource = MusicBrainzResource.GENRE
    var titleState by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    // TODO: viewmodel to record visit kept crashing
    LaunchedEffect(key1 = genreId) {
        titleState = title ?: "[???]"
    }

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = resource,
                title = titleState,
                onBack = onBack,
            )
        },
    ) { innerPadding ->
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
                Text(text = stringResource(id = R.string.open_in_browser))
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    PreviewTheme {
        GenreScaffold(
            genreId = "1",
            title = "Pop",
            onBack = { }
        )
    }
}
