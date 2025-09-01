package ly.david.musicsearch.data.repository.work

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkAttributeMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.helpers.TestWorkRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class WorkRepositoryImplTest : KoinTest, TestWorkRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val workDao: WorkDao by inject()
    override val workAttributeDao: WorkAttributeDao by inject()
    override val aliasDao: AliasDao by inject()

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createWorkRepository(
            musicBrainzModel = WorkMusicBrainzNetworkModel(
                id = "717a6517-290e-3696-942a-aba233ffc398",
                name = "君の知らない物語",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupWork(
            workId = "717a6517-290e-3696-942a-aba233ffc398",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            WorkDetailsModel(
                id = "717a6517-290e-3696-942a-aba233ffc398",
                name = "君の知らない物語",
                lastUpdated = testDateTimeInThePast,
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createWorkRepository(
            musicBrainzModel = WorkMusicBrainzNetworkModel(
                id = "717a6517-290e-3696-942a-aba233ffc398",
                name = "君の知らない物語",
                type = "Song",
                typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
                languages = listOf("jpn"),
                iswcs = listOf("T-903.769.392-8"),
                attributes = listOf(
                    WorkAttributeMusicBrainzModel(
                        type = "SUISA ID",
                        typeId = "034f35ae-d250-3749-95e7-854e606d5960",
                        value = "006685 500 19",
                    ),
                    WorkAttributeMusicBrainzModel(
                        type = "SPA ID",
                        typeId = "f15e9a05-231e-415b-8b7d-8ec44c736bde",
                        value = "1351887",
                    ),
                    WorkAttributeMusicBrainzModel(
                        type = "SIAE ID",
                        typeId = "2e4f9e12-d094-4bd0-b4dd-a560cc5c4977",
                        value = "9882301500",
                    ),
                    WorkAttributeMusicBrainzModel(
                        type = "GEMA ID",
                        typeId = "01eeee67-f514-3801-bdce-279e04872f91",
                        value = "11277241-001",
                    ),
                    WorkAttributeMusicBrainzModel(
                        type = "COMPASS ID",
                        typeId = "5ea37343-be89-4cd0-8a37-f471738df641",
                        value = "6996545",
                    ),
                    WorkAttributeMusicBrainzModel(
                        type = "JASRAC ID",
                        typeId = "31048fcc-3dbb-3979-8f85-805afb933e0c",
                        value = "161-1598-8",
                    ),
                ),
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "6ea47967-a4d4-4a20-8f91-76d2aa9be8b2",
                            resource = "http://kids.utamap.com/pc/showkasi_pc.php?surl=E12387",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "31bcc221-ed6d-4e96-b357-f50ead407b91",
                            resource = "http://lyric.evesta.jp/l79a26e.html",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "756081cd-7baf-4925-982e-bd97b927090b",
                            resource = "https://j-lyric.net/artist/a054f0a/l024c87.html",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "6165fd43-2792-4dfa-8a19-966955880e21",
                            resource = "https://kashinavi.com/song_view.html?37443",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "8afd628c-6e91-4d02-8687-3aa8c6153a1f",
                            resource = "https://petitlyrics.com/lyrics/136544",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "c2515300-2774-45aa-a5ef-9f0fdd02b934",
                            resource = "https://petitlyrics.com/lyrics/17763",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "d027f956-33f2-4f9f-912f-9a8109900554",
                            resource = "https://utaten.com/lyric/jb70908028/",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "c93bcd02-08b0-42c6-b878-eba62c64ce61",
                            resource = "https://utaten.com/lyric/supercell/君の知らない物語",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "93c15008-8020-4fa3-b894-37571d7d7fb4",
                            resource = "https://www.joysound.com/web/search/song/133769",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "3f8747c7-3c36-45a1-9990-6003bec56ffc",
                            resource = "https://www.musixmatch.com/lyrics/supercell/君の知らない物語",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "689eb1a6-2551-4ed0-9499-1eb4e217fece",
                            resource = "https://www.utamap.com/showkasi.php?surl=E12387",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "lyrics",
                        typeId = "e38e65aa-75e0-42ba-ace0-072aeb91a538",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "db86680c-9f2c-422b-8b36-928516b2f4a1",
                            resource = "https://www.uta-net.com/song/82447/",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikidata",
                        typeId = "587fdd8f-080e-46a9-97af-6425ebbcb3a2",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "7fb958bb-035c-4405-ac74-65dd33af6ac4",
                            resource = "https://www.wikidata.org/wiki/Q1131046",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupWork(
            workId = "717a6517-290e-3696-942a-aba233ffc398",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            WorkDetailsModel(
                id = "717a6517-290e-3696-942a-aba233ffc398",
                name = "君の知らない物語",
                lastUpdated = testDateTimeInThePast,
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupWork(
            workId = "717a6517-290e-3696-942a-aba233ffc398",
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            WorkDetailsModel(
                id = "717a6517-290e-3696-942a-aba233ffc398",
                name = "君の知らない物語",
                type = "Song",
                languages = persistentListOf("jpn"),
                iswcs = persistentListOf("T-903.769.392-8"),
                attributes = persistentListOf(
                    WorkAttributeUiModel(
                        type = "COMPASS ID",
                        typeId = "5ea37343-be89-4cd0-8a37-f471738df641",
                        value = "6996545",
                    ),
                    WorkAttributeUiModel(
                        type = "GEMA ID",
                        typeId = "01eeee67-f514-3801-bdce-279e04872f91",
                        value = "11277241-001",
                    ),
                    WorkAttributeUiModel(
                        type = "JASRAC ID",
                        typeId = "31048fcc-3dbb-3979-8f85-805afb933e0c",
                        value = "161-1598-8",
                    ),
                    WorkAttributeUiModel(
                        type = "SIAE ID",
                        typeId = "2e4f9e12-d094-4bd0-b4dd-a560cc5c4977",
                        value = "9882301500",
                    ),
                    WorkAttributeUiModel(
                        type = "SPA ID",
                        typeId = "f15e9a05-231e-415b-8b7d-8ec44c736bde",
                        value = "1351887",
                    ),
                    WorkAttributeUiModel(
                        type = "SUISA ID",
                        typeId = "034f35ae-d250-3749-95e7-854e606d5960",
                        value = "006685 500 19",
                    ),
                ),
                lastUpdated = testDateTimeInThePast,
                urls = persistentListOf(
                    RelationListItemModel(
                        id = "7fb958bb-035c-4405-ac74-65dd33af6ac4_25",
                        linkedEntityId = "7fb958bb-035c-4405-ac74-65dd33af6ac4",
                        label = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q1131046",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "6ea47967-a4d4-4a20-8f91-76d2aa9be8b2_13",
                        linkedEntityId = "6ea47967-a4d4-4a20-8f91-76d2aa9be8b2",
                        label = "lyrics page",
                        name = "http://kids.utamap.com/pc/showkasi_pc.php?surl=E12387",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "31bcc221-ed6d-4e96-b357-f50ead407b91_14",
                        linkedEntityId = "31bcc221-ed6d-4e96-b357-f50ead407b91",
                        label = "lyrics page",
                        name = "http://lyric.evesta.jp/l79a26e.html",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "756081cd-7baf-4925-982e-bd97b927090b_15",
                        linkedEntityId = "756081cd-7baf-4925-982e-bd97b927090b",
                        label = "lyrics page",
                        name = "https://j-lyric.net/artist/a054f0a/l024c87.html",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "6165fd43-2792-4dfa-8a19-966955880e21_16",
                        linkedEntityId = "6165fd43-2792-4dfa-8a19-966955880e21",
                        label = "lyrics page",
                        name = "https://kashinavi.com/song_view.html?37443",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "8afd628c-6e91-4d02-8687-3aa8c6153a1f_17",
                        linkedEntityId = "8afd628c-6e91-4d02-8687-3aa8c6153a1f",
                        label = "lyrics page",
                        name = "https://petitlyrics.com/lyrics/136544",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "c2515300-2774-45aa-a5ef-9f0fdd02b934_18",
                        linkedEntityId = "c2515300-2774-45aa-a5ef-9f0fdd02b934",
                        label = "lyrics page",
                        name = "https://petitlyrics.com/lyrics/17763",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "d027f956-33f2-4f9f-912f-9a8109900554_19",
                        linkedEntityId = "d027f956-33f2-4f9f-912f-9a8109900554",
                        label = "lyrics page",
                        name = "https://utaten.com/lyric/jb70908028/",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "c93bcd02-08b0-42c6-b878-eba62c64ce61_20",
                        linkedEntityId = "c93bcd02-08b0-42c6-b878-eba62c64ce61",
                        label = "lyrics page",
                        name = "https://utaten.com/lyric/supercell/君の知らない物語",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "93c15008-8020-4fa3-b894-37571d7d7fb4_21",
                        linkedEntityId = "93c15008-8020-4fa3-b894-37571d7d7fb4",
                        label = "lyrics page",
                        name = "https://www.joysound.com/web/search/song/133769",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "3f8747c7-3c36-45a1-9990-6003bec56ffc_22",
                        linkedEntityId = "3f8747c7-3c36-45a1-9990-6003bec56ffc",
                        label = "lyrics page",
                        name = "https://www.musixmatch.com/lyrics/supercell/君の知らない物語",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "689eb1a6-2551-4ed0-9499-1eb4e217fece_23",
                        linkedEntityId = "689eb1a6-2551-4ed0-9499-1eb4e217fece",
                        label = "lyrics page",
                        name = "https://www.utamap.com/showkasi.php?surl=E12387",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                    RelationListItemModel(
                        id = "db86680c-9f2c-422b-8b36-928516b2f4a1_24",
                        linkedEntityId = "db86680c-9f2c-422b-8b36-928516b2f4a1",
                        label = "lyrics page",
                        name = "https://www.uta-net.com/song/82447/",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = false,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
