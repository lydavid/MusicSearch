package ly.david.musicsearch.shared.feature.settings.internal.listens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.listens
import musicsearch.ui.common.generated.resources.reset
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListensSettingsUi(
    state: ListensSettingsUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                title = stringResource(Res.string.listens),
                onBack = {
                    eventSink(ListensSettingsUiEvent.NavigateUp)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Text(text = "Number of listens in details: ${state.numberOfListensInDetails}")
                Slider(
                    value = state.numberOfListensInDetails.toFloat(),
                    onValueChange = { eventSink(ListensSettingsUiEvent.UpdateNumberOfListensInDetails(it.toLong())) },
                    steps = 98,
                    valueRange = 1f..100f,
                )
            }

            SettingSwitch(
                header = "Include client and version when submitting listens",
                checked = state.submitClientAndVersionWithListen,
                onCheckedChange = {
                    eventSink(ListensSettingsUiEvent.ToggleIncludeClientAndVersion)
                },
            )

            TextButton(
                onClick = {
                    eventSink(
                        ListensSettingsUiEvent.Reset,
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = stringResource(Res.string.reset),
                )
            }
        }
    }
}
