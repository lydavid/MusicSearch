package ly.david.mbjc.ui.work.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Locale
import ly.david.musicsearch.data.core.common.ifNotNullOrEmpty
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.work.WorkAttributeUiModel
import ly.david.musicsearch.data.core.work.WorkScaffoldModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun WorkDetailsScreen(
    modifier: Modifier = Modifier,
    work: WorkScaffoldModel,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState
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
                language?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.language,
                        text = Locale(it).displayLanguage,
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
                onItemClick = onItemClick
            )
        }
    }
}

// region Previews
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            WorkDetailsScreen(
                work = WorkScaffoldModel(
                    id = "w1",
                    name = "Work",
                    type = "Song",
                    language = "eng",
                    iswcs = listOf(
                        "T-101.238.335-4",
                        "T-101.238.335-5",
                    ),
                    attributes = listOf(
                        WorkAttributeUiModel(
                            type = "AKM ID",
                            typeId = "a",
                            value = "1234567",
                        )
                    ),
                    urls = listOf(
                        RelationListItemModel(
                            id = "1",
                            linkedEntity = MusicBrainzEntity.URL,
                            linkedEntityId = "2",
                            label = "license",
                            name = "https://genius.com/Shaggy-bonafide-girl-lyrics",
                        )
                    )
                )
            )
        }
    }
}
// endregion
