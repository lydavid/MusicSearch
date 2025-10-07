package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTracksByReleaseUi() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    listOf(
                        ListSeparator(
                            id = "1",
                            text = "1・CD",
                        ),
                        TrackListItemModel(
                            id = "3d116c5d-e4c4-4995-91b4-8a18a72d9a1d",
                            position = 1,
                            number = "1",
                            name = "花一匁",
                            aliases = persistentListOf(
                                BasicAlias(
                                    name = "Hanaichi Monnme",
                                    locale = "en",
                                    isPrimary = true,
                                ),
                            ),
                            length = 130000,
                            formattedArtistCredits = "ずっと真夜中でいいのに。",
                            mediumId = 1,
                            mediumPosition = 1,
                            visited = true,
                            listenCount = 10,
                        ),
                        TrackListItemModel(
                            id = "cd5c37b3-dcfd-483f-9f70-5aa95c71936b",
                            position = 2,
                            number = "2",
                            name = "猫リセット",
                            aliases = persistentListOf(
                                BasicAlias(
                                    name = "Neko Reset",
                                    locale = "en",
                                    isPrimary = true,
                                ),
                            ),
                            length = 339000,
                            formattedArtistCredits = "ずっと真夜中でいいのに。",
                            mediumId = 1,
                            mediumPosition = 1,
                            visited = false,
                            listenCount = 2,
                        ),
                        ListSeparator(
                            id = "2",
                            text = "2・CD",
                        ),
                        TrackListItemModel(
                            id = "1af38b9b-5d34-3350-98ba-33276886b3be",
                            position = 1,
                            number = "1",
                            name = "花一匁 (Instrumental)",
                            length = 16000,
                            formattedArtistCredits = "ずっと真夜中でいいのに。",
                            mediumId = 2,
                            mediumPosition = 2,
                            listenCount = 0,
                        ),
                        ListSeparator(
                            id = "3",
                            text = "3・Blu-ray (ROAD GAME『テクノプア』~叢雲のつるぎ~)",
                        ),
                        TrackListItemModel(
                            id = "3333",
                            position = 1,
                            number = "1",
                            name = "叢雲開幕",
                            length = 324000,
                            formattedArtistCredits = "ずっと真夜中でいいのに。",
                            mediumId = 3,
                            mediumPosition = 3,
                            listenCount = 0,
                        ),
                    ),
                ),
            )

            TracksByReleaseUi(
                lazyPagingItems = items.collectAsLazyPagingItems(),
                mostListenedTrackCount = 10,
                collapsedMediumIds = setOf(2),
            )
        }
    }
}
