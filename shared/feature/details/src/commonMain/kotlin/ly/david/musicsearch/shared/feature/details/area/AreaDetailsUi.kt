package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection

@Composable
internal fun AreaDetailsUi(
    area: AreaScaffoldModel,
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
