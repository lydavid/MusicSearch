package ly.david.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.data.core.common.ifNotNullOrEmpty
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun LifeSpanText(
    lifeSpan: LifeSpanUiModel?,
    heading: String,
    modifier: Modifier = Modifier,
    beginHeading: String = "",
    endHeading: String = "",
    filterText: String = "",
) {
    lifeSpan?.run {
        Column(
            modifier = modifier,
        ) {
            val beginDate = begin
            if (beginDate == end && beginDate != null) {
                TextWithHeading(
                    heading = heading,
                    text = beginDate,
                    filterText = filterText,
                )
            } else {
                begin?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = beginHeading,
                        text = it,
                        filterText = filterText,
                    )
                }
                end?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = endHeading,
                        text = it,
                        filterText = filterText,
                    )
                }
            }
        }
    }
}

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
