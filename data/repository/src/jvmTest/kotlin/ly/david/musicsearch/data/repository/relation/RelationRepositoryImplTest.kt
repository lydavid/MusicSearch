package ly.david.musicsearch.data.repository.relation

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.AttributeValue
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class RelationRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val relationsMetadataDao: RelationsMetadataDao by inject()
    private val detailsMetadataDao: DetailsMetadataDao by inject()
    private val relationDao: RelationDao by inject()

    private fun createRepository(
        lookupApi: LookupApi,
    ): RelationRepository {
        return RelationRepositoryImpl(
            lookupApi = lookupApi,
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
    }

    @Test
    fun `is ordered by ordering-key if it exists`() = runTest {
        val relationRepository = createRepository(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupSeries(
                    seriesId: String,
                    include: String?,
                ): SeriesMusicBrainzNetworkModel {
                    return SeriesMusicBrainzNetworkModel(
                        id = "eca82a1b-1efa-4d6b-9278-e278523267f8",
                        name = "東方Project",
                        type = "Release group series",
                        relations = listOf(
                            RelationMusicBrainzModel(
                                type = "publishes series",
                                typeId = "1cd0342c-69a1-4f97-8471-46748f8ecde1",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.LABEL,
                                label = LabelMusicBrainzNetworkModel(
                                    id = "bad6d0fa-938e-45a2-95fd-b37ea37b783c",
                                    name = "上海アリス幻樂団",
                                    type = "Original Production",
                                    disambiguation = "dōjin game developer",
                                ),
                            ),
                            RelationMusicBrainzModel(
                                type = "publishes series",
                                typeId = "1cd0342c-69a1-4f97-8471-46748f8ecde1",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.LABEL,
                                label = LabelMusicBrainzNetworkModel(
                                    id = "09676c82-b1d0-4bb1-b827-91f4a8e52cb3",
                                    name = "黄昏フロンティア",
                                    type = "Original Production",
                                ),
                            ),
                            RelationMusicBrainzModel(
                                type = "part of",
                                typeId = "01018437-91d8-36b9-bf89-3f885d53b5bd",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.RELEASE_GROUP,
                                releaseGroup = ReleaseGroupMusicBrainzNetworkModel(
                                    id = "b22e3f3e-6c90-3df9-915f-12d8f86c240b",
                                    name = "東方封魔録 〜 Story of Eastern Wonderland",
                                    primaryType = null,
                                    primaryTypeId = null,
                                    secondaryTypes = emptyList(),
                                    secondaryTypeIds = emptyList(),
                                    firstReleaseDate = "1997-08-15",
                                    disambiguation = "",
                                ),
                                attributes = listOf("number"),
                                attributeValues = AttributeValue(
                                    number = "2",
                                ),
                                orderingKey = 2,
                            ),
                            RelationMusicBrainzModel(
                                type = "part of",
                                typeId = "01018437-91d8-36b9-bf89-3f885d53b5bd",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.RELEASE_GROUP,
                                releaseGroup = ReleaseGroupMusicBrainzNetworkModel(
                                    id = "5d286f5b-7cc3-3f78-b1cf-a24d496af34b",
                                    name = "東方靈異伝 〜 Highly Responsive to Prayers",
                                    primaryType = null,
                                    primaryTypeId = null,
                                    secondaryTypes = emptyList(),
                                    secondaryTypeIds = emptyList(),
                                    firstReleaseDate = "1996-11",
                                    disambiguation = "",
                                ),
                                attributes = listOf("number"),
                                attributeValues = AttributeValue(
                                    number = "1",
                                ),
                                orderingKey = 1,
                            ),
                            RelationMusicBrainzModel(
                                type = "subseries",
                                typeId = "a3af4c16-de83-4d63-b9b8-77e074c9babe",
                                direction = Direction.FORWARD,
                                targetType = SerializableMusicBrainzEntity.SERIES,
                                series = SeriesMusicBrainzNetworkModel(
                                    id = "fbca86fc-1509-40d6-b985-f50e45796187",
                                    name = "ZUN's Music Collection",
                                    type = "Release group series",
                                    typeId = "4c1c4949-7b6c-3a2d-9d54-a50a27e4fa77",
                                ),
                            ),
                        ),
                    )
                }
            },
        )

        relationRepository.observeEntityRelationships(
            entity = MusicBrainzEntity.SERIES,
            entityId = "eca82a1b-1efa-4d6b-9278-e278523267f8",
            query = "",
            lastUpdated = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    RelationListItemModel(
                        id = "bad6d0fa-938e-45a2-95fd-b37ea37b783c_5",
                        linkedEntityId = "bad6d0fa-938e-45a2-95fd-b37ea37b783c",
                        label = "publishing label",
                        name = "上海アリス幻樂団",
                        disambiguation = "dōjin game developer",
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.LABEL,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    RelationListItemModel(
                        id = "09676c82-b1d0-4bb1-b827-91f4a8e52cb3_6",
                        linkedEntityId = "09676c82-b1d0-4bb1-b827-91f4a8e52cb3",
                        label = "publishing label",
                        name = "黄昏フロンティア",
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.LABEL,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    RelationListItemModel(
                        id = "5d286f5b-7cc3-3f78-b1cf-a24d496af34b_1",
                        linkedEntityId = "5d286f5b-7cc3-3f78-b1cf-a24d496af34b",
                        label = "has parts",
                        name = "東方靈異伝 〜 Highly Responsive to Prayers",
                        disambiguation = "",
                        attributes = "number: 1",
                        linkedEntity = MusicBrainzEntity.RELEASE_GROUP,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    RelationListItemModel(
                        id = "b22e3f3e-6c90-3df9-915f-12d8f86c240b_2",
                        linkedEntityId = "b22e3f3e-6c90-3df9-915f-12d8f86c240b",
                        label = "has parts",
                        name = "東方封魔録 〜 Story of Eastern Wonderland",
                        disambiguation = "",
                        attributes = "number: 2",
                        linkedEntity = MusicBrainzEntity.RELEASE_GROUP,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    RelationListItemModel(
                        id = "fbca86fc-1509-40d6-b985-f50e45796187_9",
                        linkedEntityId = "fbca86fc-1509-40d6-b985-f50e45796187",
                        label = "subseries",
                        name = "ZUN's Music Collection",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.SERIES,
                        visited = false,
                        isForwardDirection = true,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
    }

    @Test
    fun `the same target entity is grouped`() = runTest {
        val relationRepository = createRepository(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupWork(
                    workId: String,
                    include: String?,
                ): WorkMusicBrainzNetworkModel {
                    return WorkMusicBrainzNetworkModel(
                        id = "2506ad88-1db3-454a-aed0-32cd5162fa1e",
                        name = "悪魔の子",
                        type = "Song",
                        language = "jpn",
                        iswcs = listOf("T-309.230.043-7"),
                        relations = listOf(
                            RelationMusicBrainzModel(
                                type = "composer",
                                typeId = "d59d99ea-23d4-4a80-b066-edca32ee158f",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.ARTIST,
                                artist = ArtistMusicBrainzNetworkModel(
                                    id = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                                    name = "ヒグチアイ",
                                    sortName = "Higuchi, Ai",
                                    type = "Person",
                                    countryCode = "JP",
                                ),
                            ),
                            RelationMusicBrainzModel(
                                type = "lyricist",
                                typeId = "3e48faba-ec01-47fd-8e89-30e81161661c",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.ARTIST,
                                artist = ArtistMusicBrainzNetworkModel(
                                    id = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                                    name = "ヒグチアイ",
                                    sortName = "Higuchi, Ai",
                                    type = "Person",
                                    countryCode = "JP",
                                ),
                            ),
                        ),
                    )
                }
            },
        )

        relationRepository.observeEntityRelationships(
            entity = MusicBrainzEntity.WORK,
            entityId = "2506ad88-1db3-454a-aed0-32cd5162fa1e",
            query = "",
            lastUpdated = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    RelationListItemModel(
                        id = "ae6c957d-c33e-4028-abdd-688bddec3be8_2",
                        linkedEntityId = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                        label = "composer, lyricist",
                        name = "ヒグチアイ",
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.ARTIST,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
    }

    @Test
    fun `the same target entity is grouped, label is repeated for different entities unlike MB`() = runTest {
        val relationRepository = createRepository(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupWork(
                    workId: String,
                    include: String?,
                ): WorkMusicBrainzNetworkModel {
                    return WorkMusicBrainzNetworkModel(
                        id = "dfe5d4e5-ee03-4a8c-b7b3-4e231dcbcf6c",
                        name = "シャルル",
                        type = "Song",
                        language = "jpn",
                        iswcs = listOf("T-921.450.584-7"),
                        relations = listOf(
                            RelationMusicBrainzModel(
                                type = "composer",
                                typeId = "d59d99ea-23d4-4a80-b066-edca32ee158f",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.ARTIST,
                                artist = ArtistMusicBrainzNetworkModel(
                                    id = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                                    name = "バルーン",
                                    disambiguation = "Vocaloid producer",
                                    sortName = "balloon",
                                    type = "Person",
                                    countryCode = "JP",
                                ),
                            ),
                            RelationMusicBrainzModel(
                                type = "lyricist",
                                typeId = "3e48faba-ec01-47fd-8e89-30e81161661c",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.ARTIST,
                                artist = ArtistMusicBrainzNetworkModel(
                                    id = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                                    name = "バルーン",
                                    disambiguation = "Vocaloid producer",
                                    sortName = "balloon",
                                    type = "Person",
                                    countryCode = "JP",
                                ),
                            ),
                            RelationMusicBrainzModel(
                                type = "premiere",
                                typeId = "5cc8cfb5-cca0-4395-a44b-b7d3c1777608",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.ARTIST,
                                artist = ArtistMusicBrainzNetworkModel(
                                    id = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                                    name = "バルーン",
                                    disambiguation = "Vocaloid producer",
                                    sortName = "balloon",
                                    type = "Person",
                                    countryCode = "JP",
                                ),
                            ),
                            RelationMusicBrainzModel(
                                type = "premiere",
                                typeId = "5cc8cfb5-cca0-4395-a44b-b7d3c1777608",
                                direction = Direction.BACKWARD,
                                targetType = SerializableMusicBrainzEntity.ARTIST,
                                artist = ArtistMusicBrainzNetworkModel(
                                    id = "2708d1f1-d8f1-45fd-a3c6-074a410e61d8",
                                    name = "v flower",
                                    disambiguation = "Vocaloid; \uD835\uDC87lower",
                                    sortName = "v flower",
                                    type = "Character",
                                    countryCode = null,
                                ),
                            ),
                        ),
                    )
                }
            },
        )

        relationRepository.observeEntityRelationships(
            entity = MusicBrainzEntity.WORK,
            entityId = "dfe5d4e5-ee03-4a8c-b7b3-4e231dcbcf6c",
            query = "",
            lastUpdated = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    RelationListItemModel(
                        id = "ae6c957d-c33e-4028-abdd-688bddec3be8_4",
                        name = "バルーン",
                        disambiguation = "Vocaloid producer",
                        linkedEntityId = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                        label = "composer, lyricist, premiered by",
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.ARTIST,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    RelationListItemModel(
                        id = "2708d1f1-d8f1-45fd-a3c6-074a410e61d8_7",
                        name = "v flower",
                        disambiguation = "Vocaloid; \uD835\uDC87lower",
                        linkedEntityId = "2708d1f1-d8f1-45fd-a3c6-074a410e61d8",
                        label = "premiered by",
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.ARTIST,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }

        // when filtering, the linked entities that were filtered out will not be grouped
        relationRepository.observeEntityRelationships(
            entity = MusicBrainzEntity.WORK,
            entityId = "dfe5d4e5-ee03-4a8c-b7b3-4e231dcbcf6c",
            query = "pre",
            lastUpdated = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    RelationListItemModel(
                        id = "ae6c957d-c33e-4028-abdd-688bddec3be8_6",
                        name = "バルーン",
                        disambiguation = "Vocaloid producer",
                        linkedEntityId = "ae6c957d-c33e-4028-abdd-688bddec3be8",
                        label = "premiered by",
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.ARTIST,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    RelationListItemModel(
                        id = "2708d1f1-d8f1-45fd-a3c6-074a410e61d8_7",
                        name = "v flower",
                        disambiguation = "Vocaloid; \uD835\uDC87lower",
                        linkedEntityId = "2708d1f1-d8f1-45fd-a3c6-074a410e61d8",
                        label = "premiered by",
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.ARTIST,
                        visited = false,
                        isForwardDirection = false,
                        lastUpdated = testDateTimeInThePast,
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
    }
}
