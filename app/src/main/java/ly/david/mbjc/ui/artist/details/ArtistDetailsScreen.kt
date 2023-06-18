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
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.R
import ly.david.ui.common.coverart.LargeImage
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.relation.RelationListItem
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.theme.PreviewTheme

@Composable
internal fun ArtistDetailsScreen(
    artist: ArtistScaffoldModel,
    modifier: Modifier = Modifier,
    artistImageUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            LargeImage(url = artistImageUrl)

            artist.run {
                InformationListSeparatorHeader(R.string.artist)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.type, text = it)
                }
                gender?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.gender, text = it)
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
                    }
                )
                // TODO: begin area
                // TODO: area
                // TODO: end area
                // TODO: isni code
                // todo: ipis code

                Spacer(modifier = Modifier.padding(bottom = 16.dp))

                ListSeparatorHeader("Links")
                urls.forEach {
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
@ExcludeFromJacocoGeneratedReport
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
                )
            )
        }
    }
}
// endregion
