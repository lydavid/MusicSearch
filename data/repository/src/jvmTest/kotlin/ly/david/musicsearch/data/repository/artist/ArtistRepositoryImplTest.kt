package ly.david.musicsearch.data.repository.artist

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.ArtistsByEntityDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.MembersAndGroups
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ArtistRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val artistDao: ArtistDao by inject()
    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val visitedDao: VisitedDao by inject()
    private val relationDao: RelationDao by inject()
    private val areaDao: AreaDao by inject()
    private val artistsByEntityDao: ArtistsByEntityDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createRepositoryWithFakeNetworkData(
        artistMusicBrainzModel: ArtistMusicBrainzModel,
    ): ArtistRepositoryImpl {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArtist(
                    artistId: String,
                    include: String?,
                ): ArtistMusicBrainzModel {
                    return artistMusicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return ArtistRepositoryImpl(
            artistDao = artistDao,
            relationRepository = relationRepository,
            areaDao = areaDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArtist(
                    artistId: String,
                    include: String?,
                ): ArtistMusicBrainzModel {
                    return artistMusicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup artist`() = runTest {
        val artistRepositoryImpl = createRepositoryWithFakeNetworkData(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                area = AreaMusicBrainzModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
            ),
        )

        val artistDetailsModel = artistRepositoryImpl.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                areaListItemModel = AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
            ),
            artistDetailsModel,
        )
    }

    @Test
    fun `lookup artist - area without iso-3166-1 code`() = runTest {
        val artistRepositoryImpl = createRepositoryWithFakeNetworkData(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                name = "David Bowie",
                type = "Person",
                gender = "Male",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1947-01-08",
                    end = "2016-01-10",
                    ended = true,
                ),
                sortName = "Bowie, David",
                area = AreaMusicBrainzModel(
                    id = "9d5dd675-3cf4-4296-9e39-67865ebee758",
                    name = "England",
                    countrySubDivisionCodes = listOf("GB-ENG"),
                ),
                ipis = listOf("00003960406", "00015471209"),
                isnis = listOf("0000000114448576", "0000000458257298"),
            ),
        )

        val artistDetailsModel = artistRepositoryImpl.lookupArtistDetails(
            "5441c29d-3602-4898-b1a1-b77fa23b8e50",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                name = "David Bowie",
                type = "Person",
                gender = "Male",
                lifeSpan = LifeSpanUiModel(
                    begin = "1947-01-08",
                    end = "2016-01-10",
                    ended = true,
                ),
                sortName = "Bowie, David",
                areaListItemModel = AreaListItemModel(
                    id = "9d5dd675-3cf4-4296-9e39-67865ebee758",
                    name = "England",
                    countryCodes = listOf(),
                ),
                ipis = listOf("00003960406", "00015471209"),
                isnis = listOf("0000000114448576", "0000000458257298"),
            ),
            artistDetailsModel,
        )
    }

    @Test
    fun `browse first, then lookup should overwrite with more data`() = runTest {
        val artistsByEntityRepositoryImpl = ArtistsByEntityRepositoryImpl(
            artistsByEntityDao,
            browseEntityCountDao,
            collectionEntityDao,
            artistDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseArtistsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                ): BrowseArtistsResponse {
                    return BrowseArtistsResponse(
                        1,
                        0,
                        listOf(
                            ArtistMusicBrainzModel(
                                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                                name = "The Beatles",
                                type = "Group",
                                lifeSpan = LifeSpanMusicBrainzModel(
                                    begin = "1960",
                                    end = "1970-04-10",
                                    ended = true,
                                ),
                                sortName = "Beatles, The",
                                area = AreaMusicBrainzModel(
                                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                                    name = "United Kingdom",
                                    countryCodes = listOf("GB"),
                                ),
                                countryCode = "GB",
                                isnis = listOf("0000000121707484"),
                            ),
                        ),
                    )
                }
            },
        )
        val flow: Flow<PagingData<ArtistListItemModel>> = artistsByEntityRepositoryImpl.observeArtistsByEntity(
            entityId = "area-id",
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(),
        )
        val artists: List<ArtistListItemModel> = flow.asSnapshot()
        assertEquals(
            1,
            artists.size,
        )
        val artistListItem = artists.first()
        assertEquals(
            ArtistListItemModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                countryCode = "GB",
            ),
            artistListItem,
        )

        val artistRepositoryImpl = createRepositoryWithFakeNetworkData(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                area = AreaMusicBrainzModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
            ),
        )
        val artistDetailsModel = artistRepositoryImpl.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                areaListItemModel = AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
            ),
            artistDetailsModel,
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseArtistRepository = createRepositoryWithFakeNetworkData(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
            ),
        )
        val sparseArtistDetailsModel = sparseArtistRepository.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
            ),
            sparseArtistDetailsModel,
        )

        val allDataArtistRepository = createRepositoryWithFakeNetworkData(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                sortName = "Beatles, The",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                area = AreaMusicBrainzModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
                relations = listOf(
                    // should not show up
                    RelationMusicBrainzModel(
                        type = "artist rename",
                        typeId = "9752bfdf-13ca-441a-a8bc-18928c600c73",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960",
                        end = "1960",
                        ended = true,
                        attributes = emptyList(),
                        artist = ArtistMusicBrainzModel(
                            id = "9fcc463b-7cfc-4ea4-a65e-bab77fc78e9a",
                            name = "The Quarrymen",
                            sortName = "Quarrymen, The",
                            type = "Group",
                            typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                            disambiguation = "British group that evolved into The Beatles",
                        ),
                    ),

                    // 9 members
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960-01",
                        end = "1961",
                        ended = true,
                        attributes = listOf("bass guitar"),
                        artist = ArtistMusicBrainzModel(
                            id = "49a51491-650e-44b3-8085-2f07ac2986dd",
                            name = "Stuart Sutcliffe",
                            sortName = "Sutcliffe, Stuart",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960-05",
                        end = "1960-06",
                        ended = true,
                        attributes = listOf("drums (drum set)"),
                        artist = ArtistMusicBrainzModel(
                            id = "f7d30d7d-9976-4d31-9907-19f3c30a206d",
                            name = "Tommy Moore",
                            sortName = "Moore, Tommy",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "English drummer, early 60s",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960-06",
                        end = "1960-06",
                        ended = true,
                        attributes = listOf("drums (drum set)"),
                        artist = ArtistMusicBrainzModel(
                            id = "6a0e143b-61bb-414f-99c8-96681be286a1",
                            name = "Norman Chapman",
                            sortName = "Chapman, Norman",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "English drummer, early 60s",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960-08-12",
                        end = "1962-08-16",
                        ended = true,
                        attributes = listOf("drums (drum set)"),
                        artist = ArtistMusicBrainzModel(
                            id = "0d4ab0f9-bbda-4ab1-ae2c-f772ffcfbea9",
                            name = "Pete Best",
                            sortName = "Best, Pete",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "original drummer in The Beatles",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960-12",
                        end = "1961-01",
                        ended = true,
                        attributes = listOf("bass guitar"),
                        artist = ArtistMusicBrainzModel(
                            id = "2629082c-19b4-42ae-b2e1-d6025ead67a0",
                            name = "Chas Newby",
                            sortName = "Newby, Chas",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "bassist in The Quarrymen",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960",
                        end = "1969-09",
                        ended = true,
                        attributes = listOf("guitar", "lead vocals", "original"),
                        artist = ArtistMusicBrainzModel(
                            id = "4d5447d7-c61c-4120-ba1b-d7f471d385b9",
                            name = "John Lennon",
                            sortName = "Lennon, John",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "The Beatles",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960",
                        end = "1970-04-10",
                        ended = true,
                        attributes = listOf("guitar", "lead vocals", "original"),
                        artist = ArtistMusicBrainzModel(
                            id = "42a8f507-8412-4611-854f-926571049fa0",
                            name = "George Harrison",
                            sortName = "Harrison, George",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "The Beatles",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1960",
                        end = "1970-04-10",
                        ended = true,
                        attributes = listOf("bass guitar", "lead vocals", "original"),
                        artist = ArtistMusicBrainzModel(
                            id = "ba550d0e-adac-4864-b88b-407cab5e76af",
                            name = "Paul McCartney",
                            sortName = "McCartney, Paul",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "The Beatles",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "member of band",
                        typeId = "5be4c609-9afa-4ea0-910b-12ffb71e3821",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.ARTIST,
                        begin = "1962-08",
                        end = "1970-04-10",
                        ended = true,
                        attributes = listOf("drums (drum set)"),
                        artist = ArtistMusicBrainzModel(
                            id = "300c4c73-33ac-4255-9d57-4e32627f5e13",
                            name = "Ringo Starr",
                            sortName = "Starr, Ringo",
                            type = "Person",
                            typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                            disambiguation = "The Beatles",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataArtistRepository.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataArtistRepository.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            true,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                areaListItemModel = AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
                membersAndGroups = MembersAndGroups(
                    previousMembersOfGroup = listOf(
                        RelationListItemModel(
                            id = "49a51491-650e-44b3-8085-2f07ac2986dd_11",
                            linkedEntityId = "49a51491-650e-44b3-8085-2f07ac2986dd",
                            label = "member of band",
                            name = "Stuart Sutcliffe",
                            disambiguation = null,
                            attributes = "bass guitar",
                            additionalInfo = "(1960-01 to 1961)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960-01", end = "1961", ended = true),
                        ),
                        RelationListItemModel(
                            id = "f7d30d7d-9976-4d31-9907-19f3c30a206d_12",
                            linkedEntityId = "f7d30d7d-9976-4d31-9907-19f3c30a206d",
                            label = "member of band",
                            name = "Tommy Moore",
                            disambiguation = "English drummer, early 60s",
                            attributes = "drums (drum set)",
                            additionalInfo = "(1960-05 to 1960-06)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960-05", end = "1960-06", ended = true),
                        ),
                        RelationListItemModel(
                            id = "6a0e143b-61bb-414f-99c8-96681be286a1_13",
                            linkedEntityId = "6a0e143b-61bb-414f-99c8-96681be286a1",
                            label = "member of band",
                            name = "Norman Chapman",
                            disambiguation = "English drummer, early 60s",
                            attributes = "drums (drum set)",
                            additionalInfo = "(1960-06)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960-06", end = "1960-06", ended = true),
                        ),
                        RelationListItemModel(
                            id = "0d4ab0f9-bbda-4ab1-ae2c-f772ffcfbea9_14",
                            linkedEntityId = "0d4ab0f9-bbda-4ab1-ae2c-f772ffcfbea9",
                            label = "member of band",
                            name = "Pete Best",
                            disambiguation = "original drummer in The Beatles",
                            attributes = "drums (drum set)",
                            additionalInfo = "(1960-08-12 to 1962-08-16)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960-08-12", end = "1962-08-16", ended = true),
                        ),
                        RelationListItemModel(
                            id = "2629082c-19b4-42ae-b2e1-d6025ead67a0_15",
                            linkedEntityId = "2629082c-19b4-42ae-b2e1-d6025ead67a0",
                            label = "member of band",
                            name = "Chas Newby",
                            disambiguation = "bassist in The Quarrymen",
                            attributes = "bass guitar",
                            additionalInfo = "(1960-12 to 1961-01)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960-12", end = "1961-01", ended = true),
                        ),
                        RelationListItemModel(
                            id = "4d5447d7-c61c-4120-ba1b-d7f471d385b9_16",
                            linkedEntityId = "4d5447d7-c61c-4120-ba1b-d7f471d385b9",
                            label = "member of band",
                            name = "John Lennon",
                            disambiguation = "The Beatles",
                            attributes = "guitar, lead vocals, original",
                            additionalInfo = "(1960 to 1969-09)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960", end = "1969-09", ended = true),
                        ),
                        RelationListItemModel(
                            id = "42a8f507-8412-4611-854f-926571049fa0_17",
                            linkedEntityId = "42a8f507-8412-4611-854f-926571049fa0",
                            label = "member of band",
                            name = "George Harrison",
                            disambiguation = "The Beatles",
                            attributes = "guitar, lead vocals, original",
                            additionalInfo = "(1960 to 1970-04-10)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960", end = "1970-04-10", ended = true),
                        ),
                        RelationListItemModel(
                            id = "ba550d0e-adac-4864-b88b-407cab5e76af_18",
                            linkedEntityId = "ba550d0e-adac-4864-b88b-407cab5e76af",
                            label = "member of band",
                            name = "Paul McCartney",
                            disambiguation = "The Beatles",
                            attributes = "bass guitar, lead vocals, original",
                            additionalInfo = "(1960 to 1970-04-10)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1960", end = "1970-04-10", ended = true),
                        ),
                        RelationListItemModel(
                            id = "300c4c73-33ac-4255-9d57-4e32627f5e13_19",
                            linkedEntityId = "300c4c73-33ac-4255-9d57-4e32627f5e13",
                            label = "member of band",
                            name = "Ringo Starr",
                            disambiguation = "The Beatles",
                            attributes = "drums (drum set)",
                            additionalInfo = "(1962-08 to 1970-04-10)",
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            visited = false,
                            isForwardDirection = false,
                            lifeSpan = LifeSpanUiModel(begin = "1962-08", end = "1970-04-10", ended = true),
                        ),
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
