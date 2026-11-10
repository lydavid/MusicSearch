package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.listitem.TaggedEntityListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

private val items = MutableStateFlow(
    PagingData.from(
        listOf(
            TaggedEntityListItemModel(
                id = "66bdd1c9-d1c5-40b7-a487-5061fffbd87d",
                entityType = MusicBrainzEntityType.ARTIST,
                entityName = "Eve",
                disambiguation = "Japanese singer-songwriter, utaite, and Vocaloid producer",
            ),
            TaggedEntityListItemModel(
                id = "fdfd04bd-f124-4d38-a219-1ad1e8714b9c",
                entityType = MusicBrainzEntityType.ARTIST,
                entityName = "tayori",
                disambiguation = "",
            ),
            TaggedEntityListItemModel(
                id = "b6db0b98-db67-4a9d-8f7e-9a4235ef0321",
                entityType = MusicBrainzEntityType.RECORDING,
                entityName = "明け星",
                disambiguation = "",
            ),
            TaggedEntityListItemModel(
                id = "c34d906b-ab72-42e3-87bc-e1a10a767bde",
                entityType = MusicBrainzEntityType.RELEASE_GROUP,
                entityName = "Null",
                disambiguation = "",
            ),
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewGenreUi() {
    PreviewTheme {
        GenreOrTagUi(
            state = GenreOrTagUiState(
                name = "j-pop",
                url = "https://musicbrainz.org/genre/eba7715e-ee26-4989-8d49-9db382955419",
                isGenre = true,
                lazyPagingItems = items.collectAsLazyPagingItems(),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagUi() {
    PreviewTheme {
        GenreOrTagUi(
            state = GenreOrTagUiState(
                name = "custom tag",
                url = "https://musicbrainz.org/tag/custom tag",
                isGenre = false,
                lazyPagingItems = items.collectAsLazyPagingItems(),
            ),
        )
    }
}
