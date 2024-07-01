package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

internal class LifeSpanPreviewParameterProvider : PreviewParameterProvider<LifeSpanUiModel> {
    override val values: Sequence<LifeSpanUiModel> = sequenceOf(
        LifeSpanUiModel(
            begin = "2022-12-15",
            end = "2022-12-16",
        ),
        LifeSpanUiModel(
            begin = "2022-12-15",
            end = "2022-12-15",
        ),
        LifeSpanUiModel(
            begin = "2022-12-15",
            end = null,
        ),
        LifeSpanUiModel(
            begin = null,
            end = "2022-12-15",
        ),
        LifeSpanUiModel(
            begin = null,
            end = null,
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(LifeSpanPreviewParameterProvider::class) lifeSpan: LifeSpanUiModel,
) {
    PreviewTheme {
        Surface {
            LifeSpanText(
                lifeSpan = lifeSpan,
                heading = "Date",
            )
        }
    }
}
