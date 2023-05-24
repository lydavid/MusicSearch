package ly.david.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.ui.common.R
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.theme.PreviewTheme

/**
 * Should be used inside a [ColumnScope].
 */
@Composable
fun LifeSpanText(
    lifeSpan: LifeSpan?,
    modifier: Modifier = Modifier,
    beginHeadingRes: Int = R.string.start_date,
    endHeadingRes: Int = R.string.end_date
) {
    lifeSpan?.run {
        Column(
            modifier = modifier
        ) {
            val beginDate = begin
            if (beginDate == end && beginDate != null) {
                TextWithHeadingRes(headingRes = R.string.date, text = beginDate)
            } else {
                begin?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = beginHeadingRes, text = it)
                }
                end?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = endHeadingRes, text = it)
                }
            }
        }
    }
}

internal class LifeSpanPreviewParameterProvider : PreviewParameterProvider<LifeSpan> {
    override val values: Sequence<LifeSpan> = sequenceOf(
        LifeSpan(
            begin = "2022-12-15",
            end = "2022-12-16"
        ),
        LifeSpan(
            begin = "2022-12-15",
            end = "2022-12-15"
        ),
        LifeSpan(
            begin = "2022-12-15",
            end = null
        ),
        LifeSpan(
            begin = null,
            end = "2022-12-15"
        ),
        LifeSpan(
            begin = null,
            end = null
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(LifeSpanPreviewParameterProvider::class) lifeSpan: LifeSpan
) {
    PreviewTheme {
        Surface {
            LifeSpanText(lifeSpan = lifeSpan)
        }
    }
}
