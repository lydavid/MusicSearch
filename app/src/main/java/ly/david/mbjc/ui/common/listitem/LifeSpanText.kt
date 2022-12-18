package ly.david.mbjc.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Should be used inside a [ColumnScope].
 */
@Composable
internal fun LifeSpanText(lifeSpan: LifeSpan?) {
    lifeSpan?.run {
        val beginDate = begin
        if (beginDate == end && beginDate != null) {
            TextWithHeadingRes(headingRes = R.string.date, text = beginDate)
        } else {
            begin?.ifNotNullOrEmpty {
                TextWithHeadingRes(headingRes = R.string.start_date, text = it)
            }
            end?.ifNotNullOrEmpty {
                TextWithHeadingRes(headingRes = R.string.end_date, text = it)
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

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(LifeSpanPreviewParameterProvider::class) lifeSpan: LifeSpan
) {
    PreviewTheme {
        Surface {
            Column {
                LifeSpanText(lifeSpan = lifeSpan)
            }
        }
    }
}
