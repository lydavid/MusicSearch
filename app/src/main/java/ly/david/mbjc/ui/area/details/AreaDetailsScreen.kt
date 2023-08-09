package ly.david.mbjc.ui.area.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.area.AreaScaffoldModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.R
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun AreaDetailsScreen(
    area: AreaScaffoldModel,
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
            area.run {
                InformationListSeparatorHeader(R.string.area)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.type, text = it)
                }
                LifeSpanText(lifeSpan = lifeSpan)
                countryCodes?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.iso_3166_1, text = it.joinToString(", "))
                }

                // TODO: api doesn't seem to include area containment
                //  but we could get its parent area via relations "part of" "backward"

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick,
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
            AreaDetailsScreen(
                area = AreaScaffoldModel(
                    id = "88f49821-05a3-3bbc-a24b-bbd6b918c07b",
                    name = "Czechoslovakia",
                    type = "Country",
                    lifeSpan = LifeSpan(
                        begin = "1918-10-28",
                        end = "1992-12-31",
                        ended = true
                    ),
                    countryCodes = listOf("XC")
                )
            )
        }
    }
}
// endregion
