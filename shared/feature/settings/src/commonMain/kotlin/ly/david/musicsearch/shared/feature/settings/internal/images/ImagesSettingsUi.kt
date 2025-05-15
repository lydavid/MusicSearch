package ly.david.musicsearch.shared.feature.settings.internal.images

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
import ly.david.musicsearch.shared.domain.DEFAULT_IMAGES_GRID_PADDING_DP
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_IMAGES_PER_ROW
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImagesSettingsUi(
    state: ImagesSettingsUiState,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                title = strings.images,
                onBack = {
                    eventSink(ImagesSettingsUiEvent.NavigateUp)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Column {
                Text(text = "Number of images per row: ${state.numberOfImagesPerRow}")
                Slider(
                    value = state.numberOfImagesPerRow.toFloat(),
                    onValueChange = { eventSink(ImagesSettingsUiEvent.UpdateNumberOfImagesPerRow(it.toInt())) },
                    steps = 7,
                    valueRange = 2f..10f,
                )
            }

            Column {
                Text(text = "Images grid padding: ${state.imagesGridPaddingDp}")
                Slider(
                    value = state.imagesGridPaddingDp.toFloat(),
                    onValueChange = { eventSink(ImagesSettingsUiEvent.UpdateImagesGridPaddingDp(it.toInt())) },
                    steps = 14,
                    valueRange = 0f..16f,
                )
            }

            TextButton(
                onClick = {
                    eventSink(ImagesSettingsUiEvent.UpdateNumberOfImagesPerRow(DEFAULT_NUMBER_OF_IMAGES_PER_ROW))
                    eventSink(ImagesSettingsUiEvent.UpdateImagesGridPaddingDp(DEFAULT_IMAGES_GRID_PADDING_DP))
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Reset",
                )
            }
        }
    }
}
