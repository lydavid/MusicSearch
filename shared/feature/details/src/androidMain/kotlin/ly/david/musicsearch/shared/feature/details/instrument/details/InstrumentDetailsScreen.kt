package ly.david.musicsearch.shared.feature.details.instrument.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.instrument.InstrumentScaffoldModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun InstrumentDetailsUi(
    instrument: InstrumentScaffoldModel,
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
            instrument.run {
                ListSeparatorHeader(strings.informationHeader(strings.instrument))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }

                ListSeparatorHeader(strings.description)
                description?.ifNotNullOrEmpty {
                    SelectionContainer {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            text = it,
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }
                }

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
private fun Preview() {
    PreviewTheme {
        Surface {
            InstrumentDetailsUi(
                instrument = InstrumentScaffoldModel(
                    id = "i1",
                    name = "baroque guitar",
                    disambiguation = "Baroque gut string guitar",
                    type = "String instrument",
                    description = "Predecessor of the modern classical guitar, it had gut strings and even gut frets. " +
                        "First described in 1555, it surpassed the Renaissance lute's popularity.",
                ),
            )
        }
    }
}
// endregion
