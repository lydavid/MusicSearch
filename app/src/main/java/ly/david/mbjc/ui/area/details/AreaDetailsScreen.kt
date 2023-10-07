package ly.david.mbjc.ui.area.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.area.AreaScaffoldModel
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.strings.LocalStrings
import ly.david.ui.common.text.TextWithHeading
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
    val strings = LocalStrings.current
    
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            area.run {
                // TODO: Consider passing the string mapping of MusicBrainzEntity to these instead
                ListSeparatorHeader(text = strings.informationHeader(strings.area))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                LifeSpanText(
                    lifeSpan = lifeSpan,
                    heading = strings.date,
                    beginHeading = strings.startDate,
                    endHeading = strings.endDate,
                    filterText = filterText,
                )
                countryCodes?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.iso31661,
                        text = it.joinToString(", "),
                        filterText = filterText,
                    )
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
@DefaultPreviews
@Composable
internal fun PreviewAreaDetailsScreen() {
    PreviewTheme {
        Surface {
            AreaDetailsScreen(
                area = AreaScaffoldModel(
                    id = "88f49821-05a3-3bbc-a24b-bbd6b918c07b",
                    name = "Czechoslovakia",
                    type = "Country",
                    lifeSpan = LifeSpanUiModel(
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
