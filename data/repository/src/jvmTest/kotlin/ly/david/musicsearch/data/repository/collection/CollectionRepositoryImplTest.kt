package ly.david.musicsearch.data.repository.collection

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.aimerArtistMusicBrainzModel
import ly.david.data.test.api.FakeCollectionApi
import ly.david.data.test.clock.FixedClock
import ly.david.data.test.zutomayoArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseCollectionsResponse
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.BrowseRemoteMetadataRepositoryImpl
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.collection.PendingEdit
import ly.david.musicsearch.shared.domain.error.Action
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Instant

private const val NEW_COLLECTION_ID = "f3fff548-8282-4c9a-9cea-0e2af40029fe"

class CollectionRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val collectionDao: CollectionDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()

    private val now = Instant.parse("1970-01-02T05:00:00Z")

    private fun createRepository(
        collectionApi: CollectionApi,
    ): CollectionRepository {
        return CollectionRepositoryImpl(
            collectionApi = collectionApi,
            collectionDao = collectionDao,
            collectionEntityDao = collectionEntityDao,
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            browseEntityCountRepository = BrowseRemoteMetadataRepositoryImpl(
                browseRemoteMetadataDao = browseRemoteMetadataDao,
            ),
            clock = FixedClock(now = now),
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
            entityType = null,
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
                            CollectionMusicBrainzNetworkModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzNetworkModel(
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
            entityType = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "2",
                    isRemote = true,
                    name = "Areas",
                    entity = MusicBrainzEntityType.AREA,
                ),
                CollectionListItemModel(
                    id = "1",
                    name = "Artists",
                    entity = MusicBrainzEntityType.ARTIST,
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
                            CollectionMusicBrainzNetworkModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzNetworkModel(
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
            entityType = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY_REVERSE,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "1",
                    name = "Artists",
                    entity = MusicBrainzEntityType.ARTIST,
                    isRemote = true,
                ),
                CollectionListItemModel(
                    id = "2",
                    isRemote = true,
                    name = "Areas",
                    entity = MusicBrainzEntityType.AREA,
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
                            CollectionMusicBrainzNetworkModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                                artistCount = 3,
                            ),
                            CollectionMusicBrainzNetworkModel(
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
            entityType = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.MOST_ENTITY_COUNT,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "1",
                    name = "Artists",
                    entity = MusicBrainzEntityType.ARTIST,
                    isRemote = true,
                    cachedEntityCount = 0, // Because we have not clicked into the collection
                ),
                CollectionListItemModel(
                    id = "2",
                    isRemote = true,
                    name = "Areas",
                    entity = MusicBrainzEntityType.AREA,
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
                            CollectionMusicBrainzNetworkModel(
                                id = "1",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzNetworkModel(
                                id = "2",
                                name = "Areas",
                                entityType = SerializableMusicBrainzEntity.AREA,
                            ),
                        ),
                    )
                }
            },
        )

        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        name = "Artists",
                        entity = MusicBrainzEntityType.ARTIST,
                        isRemote = true,
                    ),
                ),
                this,
            )
        }
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
                            CollectionMusicBrainzNetworkModel(
                                id = "1",
                                name = "Won't see me",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzNetworkModel(
                                id = "2",
                                name = "Artists",
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                            CollectionMusicBrainzNetworkModel(
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
            entityType = null,
            query = "art",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        )
        val collections: List<CollectionListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            listOf(
                CollectionListItemModel(
                    id = "2",
                    name = "Artists",
                    entity = MusicBrainzEntityType.ARTIST,
                    isRemote = true,
                ),
                CollectionListItemModel(
                    id = "3",
                    name = "art",
                    entity = MusicBrainzEntityType.ARTIST,
                    isRemote = true,
                ),
            ),
            collections,
        )
    }

    private val newCollection = CollectionListItemModel(
        id = NEW_COLLECTION_ID,
        isRemote = false,
        name = "New collection",
        entity = MusicBrainzEntityType.ARTIST,
    )

    private fun testAddingACollection(repository: CollectionRepository) = runTest {
        val flow: Flow<PagingData<CollectionListItemModel>> = repository.observeAllCollections(
            username = "",
            entityType = null,
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
            collection = newCollection,
        )
        collections = flow.asSnapshot()
        Assert.assertEquals(
            collections,
            listOf(newCollection),
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
        repository.markDeletedCollections(
            collectionIds = listOf(newCollection.id),
        )
        repository.observeAllCollections(
            username = "",
            entityType = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf<CollectionListItemModel>(),
                this,
            )
        }

        repository.unMarkDeletedCollections()
        repository.observeAllCollections(
            username = "",
            entityType = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    newCollection,
                ),
                this,
            )
        }

        repository.markDeletedCollections(
            collectionIds = listOf(NEW_COLLECTION_ID),
        )
        repository.deleteCollectionsMarkedForDeletion()
        repository.observeAllCollections(
            username = "",
            entityType = null,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf<CollectionListItemModel>(),
                this,
            )
        }
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

    @Test
    fun failToAddToRemoteCollection() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = listOf(
                            CollectionMusicBrainzNetworkModel(
                                id = "1",
                                name = remoteCollectionName,
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                        ),
                    )
                }

                override suspend fun addToCollection(
                    collectionId: String,
                    resourceUriPlural: String,
                    mbids: List<String>,
                ) {
                    throw HandledException(
                        userMessage = "error",
                        errorType = ErrorType.Unauthorized,
                        errorResolution = ErrorResolution.Login,
                    )
                }
            },
        )
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection,
                ),
                this,
            )
        }

        repository.observeEntityIsInACollection(entity1Id).test {
            Assert.assertEquals(false, awaitItem())

            repository.addToCollection(
                collection = remoteCollection,
                entityType = MusicBrainzEntityType.ARTIST,
                collectableIds = listOf(entity1Id),
            ).test {
                Assert.assertEquals(
                    Feedback.Loading(
                        data = EditACollectionFeedback.SyncingWithMusicBrainz,
                        time = now,
                    ),
                    awaitItem(),
                )
                Assert.assertEquals(
                    Feedback.Error(
                        data = EditACollectionFeedback.FailedToAdd(
                            collectionName = remoteCollectionName,
                            errorMessage = "error",
                        ),
                        errorResolution = ErrorResolution.Login,
                        action = Action.Login,
                        time = now,
                    ),
                    awaitItem(),
                )
                awaitComplete()
            }
        }
    }

    val remoteCollectionName = "Artists"
    val remoteCollection = CollectionListItemModel(
        id = "1",
        name = remoteCollectionName,
        entity = MusicBrainzEntityType.ARTIST,
        isRemote = true,
    )
    val entity1Id = zutomayoArtistMusicBrainzNetworkModel.id
    val entity2Id = aimerArtistMusicBrainzModel.id
    val localCollectionName = "Local Artists"
    val localCollection = CollectionListItemModel(
        id = "2",
        name = localCollectionName,
        entity = MusicBrainzEntityType.ARTIST,
        isRemote = false,
    )

    @Test
    fun `edit collections`() = runTest {
        val repository = createRepository(
            collectionApi = object : FakeCollectionApi() {
                override suspend fun browseCollectionsByUser(
                    username: String,
                    limit: Int,
                    offset: Int,
                    include: String?,
                ): BrowseCollectionsResponse {
                    return BrowseCollectionsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = listOf(
                            CollectionMusicBrainzNetworkModel(
                                id = "1",
                                name = remoteCollectionName,
                                entityType = SerializableMusicBrainzEntity.ARTIST,
                            ),
                        ),
                    )
                }
            },
        )
        addToRemoteCollection(repository)
        addToLocalAndDeleteFromRemoteCollection(repository)
        deleteFromMultipleCollections(repository)
        addToMultipleCollections(repository)
        deleteFromLocal(repository)
        addMultipleToCollection(repository)
    }

    fun addToRemoteCollection(repository: CollectionRepository) = runTest {
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection,
                ),
                this,
            )
        }

        repository.observeEntityIsInACollection(entity1Id).test {
            Assert.assertEquals(false, awaitItem())

            repository.addToCollection(
                collection = remoteCollection,
                entityType = MusicBrainzEntityType.ARTIST,
                collectableIds = listOf(entity1Id),
            ).test {
                Assert.assertEquals(
                    Feedback.Loading(
                        data = EditACollectionFeedback.SyncingWithMusicBrainz,
                        time = now,
                    ),
                    awaitItem(),
                )
                Assert.assertEquals(
                    Feedback.Success(
                        data = EditACollectionFeedback.AddedOne(collectionName = remoteCollectionName),
                        time = now,
                    ),
                    awaitItem(),
                )
                awaitComplete()
            }
            Assert.assertEquals(true, awaitItem())
        }

        repository.addToCollection(
            collection = remoteCollection,
            entityType = MusicBrainzEntityType.ARTIST,
            collectableIds = listOf(entity1Id),
        ).test {
            Assert.assertEquals(
                Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = now,
                ),
                awaitItem(),
            )
            Assert.assertEquals(
                Feedback.Success(
                    data = EditACollectionFeedback.AlreadyIn(collectionName = remoteCollectionName),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }

        repository.addToCollection(
            collection = remoteCollection,
            entityType = MusicBrainzEntityType.ARTIST,
            collectableIds = listOf(entity1Id, entity2Id),
        ).test {
            Assert.assertEquals(
                Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = now,
                ),
                awaitItem(),
            )
            Assert.assertEquals(
                Feedback.Success(
                    data = EditACollectionFeedback.AddedMultipleToCollectionSomeAlreadyAdded(
                        newInsertions = 1,
                        collectionName = remoteCollectionName,
                        countAlreadyAdded = 1,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
    }

    fun addToLocalAndDeleteFromRemoteCollection(repository: CollectionRepository) = runTest {
        repository.insertLocal(
            localCollection,
        )
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection.copy(
                        cachedEntityCount = 2,
                    ),
                    localCollection,
                ),
                this,
            )
        }

        repository.applyAllEdits(
            pendingEdits = mapOf(
                "1" to remoteCollection.copy(
                    pendingEdit = PendingEdit.Remove,
                ),
                "2" to localCollection.copy(
                    pendingEdit = PendingEdit.Add,
                ),
            ),
            entityType = MusicBrainzEntityType.ARTIST,
            collectableIds = listOf(entity1Id),
        ).test {
            Assert.assertEquals(
                Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = now,
                ),
                awaitItem(),
            )
            Assert.assertEquals(
                Feedback.Success(
                    data = EditACollectionFeedback.EditedMultipleCollections(
                        count = 1,
                        countCollectionsInsertedTo = 1,
                        countCollectionsDeletedFrom = 1,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection.copy(
                        cachedEntityCount = 1,
                    ),
                    localCollection.copy(
                        cachedEntityCount = 1,
                    ),
                ),
                this,
            )
        }
    }

    fun deleteFromMultipleCollections(repository: CollectionRepository) = runTest {
        repository.applyAllEdits(
            pendingEdits = mapOf(
                "1" to remoteCollection.copy(
                    pendingEdit = PendingEdit.Remove,
                ),
                "2" to localCollection.copy(
                    pendingEdit = PendingEdit.Remove,
                ),
            ),
            entityType = MusicBrainzEntityType.ARTIST,
            collectableIds = listOf(entity1Id, entity2Id),
        ).test {
            Assert.assertEquals(
                Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = now,
                ),
                awaitItem(),
            )
            Assert.assertEquals(
                Feedback.Success(
                    data = EditACollectionFeedback.DeletedFromMultipleCollections(
                        count = 2,
                        countCollectionsDeletedFrom = 2,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection,
                    localCollection,
                ),
                this,
            )
        }
    }

    fun addToMultipleCollections(repository: CollectionRepository) = runTest {
        repository.applyAllEdits(
            pendingEdits = mapOf(
                "1" to remoteCollection.copy(
                    pendingEdit = PendingEdit.Add,
                ),
                "2" to localCollection.copy(
                    pendingEdit = PendingEdit.Add,
                ),
            ),
            entityType = MusicBrainzEntityType.ARTIST,
            collectableIds = listOf(entity1Id, entity2Id),
        ).test {
            Assert.assertEquals(
                Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = now,
                ),
                awaitItem(),
            )
            Assert.assertEquals(
                Feedback.Success(
                    data = EditACollectionFeedback.AddedToMultipleCollections(
                        count = 2,
                        countCollectionsInsertedTo = 2,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection.copy(
                        cachedEntityCount = 2,
                    ),
                    localCollection.copy(
                        cachedEntityCount = 2,
                    ),
                ),
                this,
            )
        }
    }

    fun deleteFromLocal(repository: CollectionRepository) = runTest {
        repository.applyAllEdits(
            pendingEdits = mapOf(
                "2" to localCollection.copy(
                    pendingEdit = PendingEdit.Remove,
                ),
            ),
            entityType = MusicBrainzEntityType.ARTIST,
            collectableIds = listOf(entity1Id, entity2Id),
        ).test {
            Assert.assertEquals(
                Feedback.Loading(
                    data = EditACollectionFeedback.Loading,
                    time = now,
                ),
                awaitItem(),
            )
            Assert.assertEquals(
                Feedback.Success(
                    data = EditACollectionFeedback.DeletedFromCollection(
                        count = 2,
                        collectionName = localCollectionName,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection.copy(
                        cachedEntityCount = 2,
                    ),
                    localCollection.copy(
                        cachedEntityCount = 0,
                    ),
                ),
                this,
            )
        }
    }

    fun addMultipleToCollection(repository: CollectionRepository) = runTest {
        repository.applyAllEdits(
            pendingEdits = mapOf(
                "2" to localCollection.copy(
                    pendingEdit = PendingEdit.Add,
                ),
            ),
            entityType = MusicBrainzEntityType.ARTIST,
            collectableIds = listOf(entity1Id, entity2Id),
        ).test {
            Assert.assertEquals(
                Feedback.Loading(
                    data = EditACollectionFeedback.Loading,
                    time = now,
                ),
                awaitItem(),
            )
            Assert.assertEquals(
                Feedback.Success(
                    data = EditACollectionFeedback.AddedMultipleToCollection(
                        newInsertions = 2,
                        collectionName = localCollectionName,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        repository.observeAllCollections(
            username = "user",
            entityType = MusicBrainzEntityType.ARTIST,
            query = "",
            showLocal = true,
            showRemote = true,
            sortOption = CollectionSortOption.ALPHABETICALLY,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    remoteCollection.copy(
                        cachedEntityCount = 2,
                    ),
                    localCollection.copy(
                        cachedEntityCount = 2,
                    ),
                ),
                this,
            )
        }
    }
}
