package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.list.AllEntitiesListUiState
import ly.david.musicsearch.ui.common.list.EntitiesListUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val releases = MutableStateFlow(
    PagingData.from(
        data = listOf(
            ReleaseListItemModel(
                id = "742eb994-aa53-4d83-b421-3d82cbfe54ef",
                name = "Gintama Best 4",
                formattedArtistCredits = "Various Artists",
            ),
            ReleaseListItemModel(
                id = "0dc738a0-8c30-4a45-9dd2-0ace47b9a832",
                name = "プライド革命",
                disambiguation = "期間生産限定盤",
                date = "2015-08-05",
                countryCode = "JP",
                formattedArtistCredits = "CHiCO with HoneyWorks",
            ),
            ReleaseListItemModel(
                id = "1f398b4a-f786-48c9-92f7-1c05ee490276",
                name = "充滿I的世界",
                disambiguation = "初回盤",
                date = "2015-11-20",
                countryCode = "TW",
                formattedArtistCredits = "CHiCO with HoneyWorks",
            ),
            LastUpdatedFooter(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
            ),
        ),
    ),
)

private val relations: MutableStateFlow<PagingData<ListItemModel>> = MutableStateFlow(
    PagingData.from(
        data = listOf(
            RelationListItemModel(
                id = "1",
                linkedEntityId = "1dc670f7-1a43-4a71-973a-2ad181f4edd4",
                linkedEntity = MusicBrainzEntityType.ARTIST,
                type = "arranger",
                name = "HoneyWorks",
            ),
            RelationListItemModel(
                id = "2",
                linkedEntityId = "9953ebf6-1836-45ef-bcca-bee2e32e29c6",
                linkedEntity = MusicBrainzEntityType.ARTIST,
                type = "vocal",
                name = "CHiCO",
                disambiguation = "collaborates with HoneyWorks",
            ),
            // 4 instruments, mix, recording
            RelationListItemModel(
                id = "3",
                linkedEntityId = "f01b9f4f-f416-44d5-bedb-b6fab5595ecf",
                linkedEntity = MusicBrainzEntityType.WORK,
                type = "performance",
                name = "プライド革命",
                aliases = persistentListOf(
                    BasicAlias(
                        name = "Pride Revolution",
                        locale = "en",
                        isPrimary = true,
                    ),
                ),
            ),
        ),
    ),
)

private val detailsModel = RecordingDetailsModel(
    id = "132a508b-624a-4f1d-b61f-f6616121bab5",
    name = "プライド革命",
    length = 235000,
    firstReleaseDate = "2015-08-05",
    isrcs = persistentListOf(
        "JPX401500068",
    ),
    lastUpdated = Instant.parse("2025-06-05T20:42:19Z"),
)

private val detailsUiState = DetailsUiState(
    tabs = recordingTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = detailsModel,
    subtitle = "Recording by CHiCO with HoneyWorks",
    detailsTabUiState = DetailsTabUiState(
        now = Instant.parse("2025-09-06T18:42:20Z"),
    ),
    allEntitiesListUiState = AllEntitiesListUiState(
        releasesListUiState = EntitiesListUiState(
            pagingDataFlow = releases,
        ),
        relationsUiState = RelationsUiState(
            pagingDataFlow = relations,
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewRecordingUiDetails() {
    PreviewWithSharedElementTransition {
        RecordingUiInternal(
            state = detailsUiState,
            entityId = "132a508b-624a-4f1d-b61f-f6616121bab5",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingUiDetailsWithListens() {
    PreviewWithSharedElementTransition {
        RecordingUiInternal(
            state = detailsUiState.copy(
                detailsModel = detailsModel.copy(
                    listenCount = 1234,
                    latestListensTimestampsMs = persistentListOf(
                        1757116212000,
                        1740055177000,
                        1600055177000,
                    ),
                    listenBrainzUrl = "https://listenbrainz.org/track/132a508b-624a-4f1d-b61f-f6616121bab5",
                ),
            ),
            entityId = "132a508b-624a-4f1d-b61f-f6616121bab5",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingUiDetailsWithZeroListens() {
    PreviewWithSharedElementTransition {
        RecordingUiInternal(
            state = detailsUiState.copy(
                detailsModel = detailsModel.copy(
                    listenCount = 0,
                    latestListensTimestampsMs = persistentListOf(),
                    listenBrainzUrl = "https://listenbrainz.org/track/132a508b-624a-4f1d-b61f-f6616121bab5",
                ),
            ),
            entityId = "132a508b-624a-4f1d-b61f-f6616121bab5",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingUiReleases() {
    PreviewWithSharedElementTransition {
        RecordingUiInternal(
            state = detailsUiState.copy(
                selectedTab = Tab.RELEASES,
            ),
            entityId = "132a508b-624a-4f1d-b61f-f6616121bab5",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingUiRelationships() {
    PreviewWithSharedElementTransition {
        RecordingUiInternal(
            state = detailsUiState.copy(
                selectedTab = Tab.RELATIONSHIPS,
            ),
            entityId = "132a508b-624a-4f1d-b61f-f6616121bab5",
        )
    }
}
