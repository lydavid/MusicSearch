package ly.david.musicsearch.shared.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.Header
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHeader
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewSearchUiSearchResults() {
    PreviewWithTransitionAndOverlays {
        val searchResults = MutableStateFlow(
            PagingData.from(
                listOf(
                    SearchHeader(
                        remoteCount = 37,
                    ),
                    ArtistListItemModel(
                        id = "9388cee2-7d57-4598-905f-106019b267d3",
                        name = "Aimer",
                        sortName = "Aimer",
                        disambiguation = "Japanese pop singer",
                        type = "Person",
                        gender = "female",
                        countryCode = "JP",
                        lifeSpan = LifeSpanUiModel(
                            ended = false,
                        ),
                        visited = true,
                    ),
                    ArtistListItemModel(
                        id = "22e5522d-84da-4168-844d-ee55655b5067",
                        name = "AIMER",
                        sortName = "AIMER",
                        disambiguation = "dubstep artist from Brisbane",
                        type = "Person",
                        gender = "female",
                        countryCode = "AU",
                        lifeSpan = LifeSpanUiModel(
                            ended = false,
                        ),
                    ),
                    ArtistListItemModel(
                        id = "4a42053d-8b57-4341-a89d-d711ff4df7c8",
                        name = "Aimer",
                        sortName = "Aimer",
                        disambiguation = "Italian guitarist",
                        type = "Person",
                        gender = "male",
                        countryCode = "IT",
                        lifeSpan = LifeSpanUiModel(
                            ended = false,
                        ),
                    ),
                    ArtistListItemModel(
                        id = "1303b976-b862-4f04-94fd-a8d444e06714",
                        name = "The Proclaimers",
                        sortName = "Proclaimers, The",
                        type = "Group",
                        countryCode = "",
                        lifeSpan = LifeSpanUiModel(
                            begin = "1983",
                            ended = false,
                        ),
                    ),
                    ArtistListItemModel(
                        id = "7c79f080-0243-4e67-8d96-e9f8fd4559b7",
                        name = "Paul Hofhaimer",
                        sortName = "Hofhaimer, Paul",
                        disambiguation = "composer",
                        type = "Person",
                        gender = "male",
                        countryCode = "AT",
                        lifeSpan = LifeSpanUiModel(
                            begin = "1459-01-25",
                            end = "1537",
                            ended = true,
                        ),
                    ),
                    ArtistListItemModel(
                        id = "cc86a0b3-e216-4807-b351-cf63c69f42dc",
                        name = "Shimon Craimer",
                        sortName = "Craimer, Shimon",
                        type = "Person",
                        countryCode = "GB",
                        lifeSpan = LifeSpanUiModel(
                            begin = "1978",
                            ended = false,
                        ),
                    ),
                    ArtistListItemModel(
                        id = "43df31b1-c38e-42e3-bbd1-f085dc362a24",
                        name = "AIMERS",
                        sortName = "AIMERS",
                        disambiguation = "South Korean boy band",
                        type = "Group",
                        countryCode = "KR",
                        lifeSpan = LifeSpanUiModel(
                            ended = false,
                        ),
                    ),
                ),
            ),
        )
        val searchHistory = MutableStateFlow(
            PagingData.empty<ListItemModel>(),
        )
        SearchUiContent(
            state = SearchUiState(
                query = "aimer",
                entity = MusicBrainzEntityType.ARTIST,
                searchResults = searchResults.collectAsLazyPagingItems(),
                searchHistory = searchHistory.collectAsLazyPagingItems(),
                eventSink = {},
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSearchUiSearchHistory() {
    PreviewWithTransitionAndOverlays {
        val searchResults = MutableStateFlow(
            PagingData.empty<ListItemModel>(),
        )
        val searchHistory = MutableStateFlow(
            PagingData.from(
                listOf(
                    Header,
                    SearchHistoryListItemModel(
                        id = "a",
                        query = "aimer",
                        entity = MusicBrainzEntityType.ARTIST,
                    ),
                ),
            ),
        )
        SearchUiContent(
            state = SearchUiState(
                query = "",
                entity = MusicBrainzEntityType.ARTIST,
                searchResults = searchResults.collectAsLazyPagingItems(),
                searchHistory = searchHistory.collectAsLazyPagingItems(),
                eventSink = {},
            ),
        )
    }
}
