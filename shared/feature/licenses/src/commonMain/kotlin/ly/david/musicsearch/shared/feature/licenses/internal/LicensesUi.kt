package ly.david.musicsearch.shared.feature.licenses.internal

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.rememberLibraries
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.shared.feature.licenses.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LicensesUi(
    state: LicensesUiState,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                onBack = { eventSink(LicensesUiEvent.NavigateUp) },
                title = strings.openSourceLicenses,
            )
        },
    ) { innerPadding ->
        LicensesUi(modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LicensesUi(
    modifier: Modifier = Modifier,
) {
    val libraries by rememberLibraries {
        // This file will be changed before building the different variants of the app.
        // Only the F-Droid variant is checked into Git, to simplify reproducible builds.
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
    )
}
