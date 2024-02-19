package ly.david.mbjc.ui.artist.details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.musicsearch.ui.image.LargeImage
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.commonlegacy.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun ArtistDetailsScreen(
    artist: ArtistScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    artistImageUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            LargeImage(
                url = artistImageUrl,
                mbid = artist.id,
            )

            artist.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.artist))
                sortName.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.sortName,
                        text = it,
                        filterText = filterText,
                    )
                }
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                gender?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.gender,
                        text = it,
                        filterText = filterText,
                    )
                }
                LifeSpanText(
                    lifeSpan = lifeSpan,
                    heading = strings.date,
                    beginHeading = when (type) {
                        "Person" -> strings.born
                        "Character" -> strings.created
                        else -> strings.founded
                    },
                    endHeading = when (type) {
                        "Person" -> strings.died
                        // Characters do not "die": https://musicbrainz.org/doc/Artist
                        else -> strings.dissolved
                    },
                    filterText = filterText,
                )

                // TODO: begin area, area, end area
//                countryCode?.ifNotNullOrEmpty {
//                    TextWithHeadingRes(headingRes = strings.area, text = it.toFlagEmoji())
//                }

                // TODO: isni code

                // todo: ipis code

                Spacer(modifier = Modifier.padding(bottom = 16.dp))

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
            ArtistDetailsScreen(
                artist = ArtistScaffoldModel(
                    id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                    name = "The Beatles",
                    type = "Group",
                    lifeSpan = LifeSpanUiModel(
                        begin = "1960",
                        end = "1970-04-10",
                        ended = true,
                    ),
                    sortName = "Beatles, The",
                ),
                artistImageUrl = "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433",
            )
        }
    }
}
// endregion
