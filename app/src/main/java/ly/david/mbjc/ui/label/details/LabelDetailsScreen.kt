package ly.david.mbjc.ui.label.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ly.david.data.core.common.ifNotNull
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.musicsearch.domain.label.LabelScaffoldModel
import ly.david.ui.common.R
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun LabelDetailsScreen(
    label: LabelScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            label.run {
                InformationListSeparatorHeader(R.string.label)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                labelCode?.ifNotNull {
                    TextWithHeadingRes(
                        headingRes = R.string.label_code,
                        text = stringResource(id = R.string.lc, it),
                        filterText = filterText,
                    )
                }

                // TODO: lifespan, founded, defunct for end

                // TODO: area

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick
                )
            }
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
            LabelDetailsScreen(
                label = LabelScaffoldModel(
                    id = "f9ada3ae-3081-44df-8581-ca27a3462b68",
                    name = "Sony BMG Music Entertainment",
                    disambiguation = "Aug 5, 2004 - Oct 1, 2008",
                    type = "Original Production",
                    labelCode = 13989
                )
            )
        }
    }
}
// endregion
