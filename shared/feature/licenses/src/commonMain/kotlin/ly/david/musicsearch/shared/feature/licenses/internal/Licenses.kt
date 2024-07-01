package ly.david.musicsearch.shared.feature.licenses.internal

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Licenses(
    state: LicensesUiState,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ScrollableTopAppBar(
                onBack = { eventSink(LicensesUiEvent.NavigateUp) },
                title = strings.openSourceLicenses,
            )
        },
    ) { innerPadding ->
        Licenses(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
internal expect fun Licenses(
    modifier: Modifier = Modifier,
)
