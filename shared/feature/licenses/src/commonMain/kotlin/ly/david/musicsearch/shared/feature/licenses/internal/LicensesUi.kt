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
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.openSourceLicenses
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import musicsearch.shared.feature.licenses.generated.resources.Res as LicensesRes
import musicsearch.ui.common.generated.resources.Res as CommonRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LicensesUi(
    state: LicensesUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                onBack = { eventSink(LicensesUiEvent.NavigateUp) },
                title = stringResource(CommonRes.string.openSourceLicenses),
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
    val libraries by produceLibraries {
        // This file will be changed before building the different variants of the app.
        // Only the F-Droid variant is checked into Git, to simplify reproducible builds.
        LicensesRes.readBytes("files/aboutlibraries.json").decodeToString()
    }
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
    )
}
