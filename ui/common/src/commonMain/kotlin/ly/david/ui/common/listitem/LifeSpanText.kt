package ly.david.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.ui.common.text.TextWithHeading

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
