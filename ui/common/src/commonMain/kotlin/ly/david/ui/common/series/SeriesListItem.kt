package ly.david.ui.common.series

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun SeriesListItem(
    series: SeriesListItemModel,
    modifier: Modifier = Modifier,
    onSeriesClick: SeriesListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Column {
                series.run {
                    Text(
                        text = name,
                        style = TextStyles.getCardBodyTextStyle(),
                    )

                    DisambiguationText(disambiguation = disambiguation)

                    type.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                }
            }
        },
        modifier = modifier.clickable { onSeriesClick(series) },
    )
}
