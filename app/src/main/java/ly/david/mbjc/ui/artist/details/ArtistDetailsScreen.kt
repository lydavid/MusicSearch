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
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.artist.ArtistScaffoldModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.R
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.relation.RelationListItem
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.image.LargeImage

@Composable
internal fun ArtistDetailsScreen(
    artist: ArtistScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    artistImageUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            LargeImage(
                url = artistImageUrl,
                mbid = artist.id
            )

            artist.run {
                InformationListSeparatorHeader(R.string.artist)
                sortName.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.sort_name,
                        text = it,
                        filterText = filterText
                    )
                }
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.type,
                        text = it,
                        filterText = filterText
                    )
                }
                gender?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.gender,
                        text = it,
                        filterText = filterText
                    )
                }
                LifeSpanText(
                    lifeSpan = lifeSpan,
                    beginHeadingRes = when (type) {
                        "Person" -> R.string.born
                        "Character" -> R.string.created
                        else -> R.string.founded
                    },
                    endHeadingRes = when (type) {
                        "Person" -> R.string.died
                        // Characters do not "die": https://musicbrainz.org/doc/Artist
                        else -> R.string.dissolved
                    },
                    filterText = filterText
                )

                // TODO: begin area, area, end area
//                countryCode?.ifNotNullOrEmpty {
//                    TextWithHeadingRes(headingRes = R.string.area, text = it.toFlagEmoji())
//                }

                // TODO: isni code

                // todo: ipis code

                Spacer(modifier = Modifier.padding(bottom = 16.dp))

                ListSeparatorHeader("Links")
                urls
                    .filter { it.name.contains(filterText) || it.label.contains(filterText) }
                    .forEach {
                        RelationListItem(
                            relation = it,
                            onItemClick = onItemClick
                        )
                    }
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
                    lifeSpan = LifeSpan(
                        begin = "1960",
                        end = "1970-04-10",
                        ended = true
                    ),
                    sortName = "Beatles, The"
                ),
                artistImageUrl = "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433"
            )
        }
    }
}
// endregion
