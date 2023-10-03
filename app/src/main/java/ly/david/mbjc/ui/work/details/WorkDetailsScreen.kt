package ly.david.mbjc.ui.work.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Locale
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.musicsearch.domain.listitem.RelationListItemModel
import ly.david.musicsearch.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.domain.work.WorkScaffoldModel
import ly.david.ui.common.R
import ly.david.ui.common.listitem.AttributesListSeparatorHeader
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.text.TextWithHeadingRes
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
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            work.run {
                InformationListSeparatorHeader(R.string.work)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                language?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.language,
                        text = Locale(it).displayLanguage,
                        filterText = filterText,
                    )
                }
                iswcs?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.iswc,
                        text = it.joinToString("\n"),
                        filterText = filterText,
                    )
                }

                if (attributes.isNotEmpty()) {
                    AttributesListSeparatorHeader(R.string.work)
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
@ExcludeFromJacocoGeneratedReport
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
