package ly.david.musicsearch.data.repository.collection

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeCollectionApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseCollectionsResponse
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.BrowseEntityCountRepositoryImpl
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

private const val NEW_COLLECTION_ID = "f3fff548-8282-4c9a-9cea-0e2af40029fe"

class CollectionRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val collectionDao: CollectionDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()

    private fun createRepository(
        collectionApi: CollectionApi,
    ): CollectionRepository {
        return CollectionRepositoryImpl(
            collectionApi = collectionApi,
            collectionDao = collectionDao,
            collectionEntityDao = collectionEntityDao,
            browseEntityCountDao = browseEntityCountDao,
            browseEntityCountRepository = BrowseEntityCountRepositoryImpl(
                browseEntityCountDao = browseEntityCountDao,
            ),
        )
    }

    @Test
    fun `empty network, no list items`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = listOf(),
                    )
                }
            },
        )

        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "user",
            entity = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            0,
            collections.size,
        )
    }

    @Test
    fun `sorted alphabetically`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 2,
                        offset = 0,
                        musicBrainzModels = listOf(
                            CollectionMusicBrainzModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzModel(
                                id = "2",
                                name = "Areas",
                                entityType = SerializableMusicBrainzEntity.AREA,
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "user",
            entity = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            2,
            collections.size,
        )
        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "2",
                    isRemote = true,
                    name = "Areas",
                    entity = MusicBrainzEntity.AREA,
                ),
                CollectionListItemModel(
                    id = "1",
                    name = "Artists",
                    entity = MusicBrainzEntity.ARTIST,
                    isRemote = true,
                ),
            ),
            collections,
        )
    }

    @Test
    fun `sorted alphabetically reverse`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 2,
                        offset = 0,
                        musicBrainzModels = listOf(
                            CollectionMusicBrainzModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzModel(
                                id = "2",
                                name = "Areas",
                                entityType = SerializableMusicBrainzEntity.AREA,
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "user",
            entity = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY_REVERSE,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            2,
            collections.size,
        )
        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "1",
                    name = "Artists",
                    entity = MusicBrainzEntity.ARTIST,
                    isRemote = true,
                ),
                CollectionListItemModel(
                    id = "2",
                    isRemote = true,
                    name = "Areas",
                    entity = MusicBrainzEntity.AREA,
                ),
            ),
            collections,
        )
    }

    @Test
    fun `sorted by most entities`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 2,
                        offset = 0,
                        musicBrainzModels = listOf(
                            CollectionMusicBrainzModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                                artistCount = 3,
                            ),
                            CollectionMusicBrainzModel(
                                id = "2",
                                name = "Areas",
                                entityType = SerializableMusicBrainzEntity.AREA,
                                areaCount = 10,
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "user",
            entity = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.MOST_ENTITY_COUNT,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            2,
            collections.size,
        )
        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "1",
                    name = "Artists",
                    entity = MusicBrainzEntity.ARTIST,
                    isRemote = true,
                    cachedEntityCount = 0, // Because we have not clicked into the collection
                ),
                CollectionListItemModel(
                    id = "2",
                    isRemote = true,
                    name = "Areas",
                    entity = MusicBrainzEntity.AREA,
                    cachedEntityCount = 0,
                ),
            ),
            collections,
        )
    }

    @Test
    fun `filtered by an entity`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 2,
                        offset = 0,
                        musicBrainzModels = listOf(
                            CollectionMusicBrainzModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzModel(
                                id = "2",
                                name = "Areas",
                                entityType = SerializableMusicBrainzEntity.AREA,
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "user",
            entity = MusicBrainzEntity.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            1,
            collections.size,
        )
        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "1",
                    name = "Artists",
                    entity = MusicBrainzEntity.ARTIST,
                    isRemote = true,
                ),
            ),
            collections,
        )
    }

    @Test
    fun `filtered by query`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 2,
                        offset = 0,
                        musicBrainzModels = listOf(
                            CollectionMusicBrainzModel(
                                id = "1",
                                name = "Won't see me",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzModel(
                                id = "2",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzModel(
                                id = "3",
                                name = "art",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "user",
            entity = null,
            query = "art",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            2,
            collections.size,
        )
        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "2",
                    name = "Artists",
                    entity = MusicBrainzEntity.ARTIST,
                    isRemote = true,
                ),
                CollectionListItemModel(
                    id = "3",
                    name = "art",
                    entity = MusicBrainzEntity.ARTIST,
                    isRemote = true,
                ),
            ),
            collections,
        )
    }

    private fun testAddingACollection(repository: CollectionRepository) = runTest {
        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "",
            entity = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        var collections: List<CollectionListItemModel> = flow.asSnapshot()
        Assert.assertEquals(
            0,
            collections.size,
        )

        repository.insertLocal(
            collection = CollectionListItemModel(
                id = NEW_COLLECTION_ID,
                isRemote = false,
                name = "New collection",
                entity = MusicBrainzEntity.ARTIST,
            ),
        )
        collections = flow.asSnapshot()
        Assert.assertEquals(
            1,
            collections.size,
        )
        Assert.assertEquals(
            collections[0],
            CollectionListItemModel(
                id = NEW_COLLECTION_ID,
                isRemote = false,
                name = "New collection",
                entity = MusicBrainzEntity.ARTIST,
            ),
        )
    }

    @Test
    fun `add local collection`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = listOf(),
                    )
                }
            },
        )

        testAddingACollection(repository)
    }

    private suspend fun testDeletingACollection(repository: CollectionRepository) {
        repository.deleteCollection(
            collectionId = NEW_COLLECTION_ID,
            collectionName = "New collection",
        )
        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "",
            entity = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()
        Assert.assertEquals(
            0,
            collections.size,
        )
    }

    @Test
    fun `delete local collection`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = listOf(),
                    )
                }
            },
        )

        testAddingACollection(repository)
        testDeletingACollection(repository)
    }

    @Test
    fun `try to access a deleted collection`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = listOf(),
                    )
                }
            },
        )

        testAddingACollection(repository)
        testDeletingACollection(repository)
        val collection = repository.getCollection(NEW_COLLECTION_ID)
        Assert.assertEquals(
            collection,
            null,
        )
    }
}
