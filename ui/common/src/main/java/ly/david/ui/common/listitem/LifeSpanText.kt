package ly.david.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.ui.common.R
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun LifeSpanText(
    lifeSpan: LifeSpanUiModel?,
    modifier: Modifier = Modifier,
    beginHeadingRes: Int = R.string.start_date,
    endHeadingRes: Int = R.string.end_date,
    filterText: String = "",
) {
    lifeSpan?.run {
        Column(
            modifier = modifier
        ) {
            val beginDate = begin
            if (beginDate == end && beginDate != null) {
                TextWithHeadingRes(
                    headingRes = R.string.date,
                    text = beginDate,
                    filterText = filterText
                )
            } else {
                begin?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = beginHeadingRes,
                        text = it,
                        filterText = filterText
                    )
                }
                end?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = endHeadingRes,
                        text = it,
                        filterText = filterText
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
            end = "2022-12-16"
        ),
        LifeSpanUiModel(
            begin = "2022-12-15",
            end = "2022-12-15"
        ),
        LifeSpanUiModel(
            begin = "2022-12-15",
            end = null
        ),
        LifeSpanUiModel(
            begin = null,
            end = "2022-12-15"
        ),
        LifeSpanUiModel(
            begin = null,
            end = null
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
            LifeSpanText(lifeSpan = lifeSpan)
        }
    }
}
