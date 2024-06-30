package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.work.WorkScaffoldModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.common.work.getDisplayLanguage

@Composable
internal fun WorkDetailsUi(
    work: WorkScaffoldModel,
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
            work.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.work))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                language?.getDisplayLanguage(strings).ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.language,
                        text = it,
                        filterText = filterText,
                    )
                }
                iswcs?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.iswc,
                        text = it.joinToString("\n"),
                        filterText = filterText,
                    )
                }

                if (attributes.isNotEmpty()) {
                    ListSeparatorHeader(strings.attributesHeader(strings.work))
                }
            }
        }

        items(work.attributes) { attribute ->
            TextWithHeading(
                heading = attribute.type,
                text = attribute.value,
                filterText = filterText,
            )
        }

        item {
            UrlsSection(
                urls = work.urls,
                filterText = filterText,
                onItemClick = onItemClick,
            )
        }
    }
}
