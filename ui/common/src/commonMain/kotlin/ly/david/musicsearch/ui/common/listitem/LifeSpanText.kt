package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.ui.common.text.TextWithHeading

@Composable
fun LifeSpanText(
    lifeSpan: LifeSpanUiModel,
    heading: String,
    modifier: Modifier = Modifier,
    beginHeading: String = "",
    endHeading: String = "",
    filterText: String = "",
) {
    lifeSpan.run {
        Column(
            modifier = modifier,
        ) {
            val beginDate = begin
            if (beginDate == end && beginDate.isNotEmpty()) {
                TextWithHeading(
                    heading = heading,
                    text = beginDate,
                    filterText = filterText,
                )
            } else {
                begin.ifNotEmpty {
                    TextWithHeading(
                        heading = beginHeading,
                        text = it,
                        filterText = filterText,
                    )
                }
                end.ifNotEmpty {
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
