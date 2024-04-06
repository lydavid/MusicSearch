package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.label.LabelScaffoldModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection

@Composable
internal fun LabelDetailsUi(
    label: LabelScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            label.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.label))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                labelCode?.ifNotNull {
                    TextWithHeading(
                        heading = strings.labelCode,
                        text = strings.lc(it),
                        filterText = filterText,
                    )
                }

                // TODO: lifespan, founded, defunct for end

                // TODO: area

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}
