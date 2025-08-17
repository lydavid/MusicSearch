package ly.david.musicsearch.data.repository.artist

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.aimerArtistMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCollaborationDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingsListRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndEntity
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ArtistCollaborationRepositoryImplTest :
    KoinTest,
    TestArtistRepository,
    TestRecordingsListRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val artistDao: ArtistDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val areaDao: AreaDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val artistCollaborationDao: ArtistCollaborationDao by inject()
    override val collectionEntityDao: CollectionEntityDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val aliasDao: AliasDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()

    @Test
    fun `lookup artist, then recordings, releases, release groups, and collaborations`() = runTest {
        val artistCollaborationRepositoryImpl = ArtistCollaborationRepositoryImpl(
            artistCollaborationDao = artistCollaborationDao,
        )

        createArtistRepository(
            artistMusicBrainzModel = aimerArtistMusicBrainzModel,
        ).lookupArtist(
            artistId = aimerArtistMusicBrainzModel.id,
            forceRefresh = false,
            testDateTimeInThePast,
        )
        artistCollaborationRepositoryImpl.getAllCollaboratingArtistsAndEntities(
            artistId = aimerArtistMusicBrainzModel.id,
            collaborationEntityType = MusicBrainzEntityType.RECORDING,
            query = "",
        ).let { collaboratingArtistsAndEntities ->
            assertEquals(
                emptyList<CollaboratingArtistAndEntity>(),
                collaboratingArtistsAndEntities,
            )
        }
        artistCollaborationRepositoryImpl.getAllCollaboratingArtistsAndEntities(
            artistId = aimerArtistMusicBrainzModel.id,
            collaborationEntityType = MusicBrainzEntityType.RELEASE,
            query = "",
        ).let { collaboratingArtistsAndEntities ->
            assertEquals(
                emptyList<CollaboratingArtistAndEntity>(),
                collaboratingArtistsAndEntities,
            )
        }
        artistCollaborationRepositoryImpl.getAllCollaboratingArtistsAndEntities(
            artistId = aimerArtistMusicBrainzModel.id,
            collaborationEntityType = MusicBrainzEntityType.RELEASE_GROUP,
            query = "",
        ).let { collaboratingArtistsAndEntities ->
            assertEquals(
                emptyList<CollaboratingArtistAndEntity>(),
                collaboratingArtistsAndEntities,
            )
        }

        createRecordingsListRepository(
            recordings = listOf(
                RecordingMusicBrainzNetworkModel(
                    id = "fea5134e-f0ee-49d5-a643-470fbc893241",
                    name = "ninelie",
                    disambiguation = "",
                    artistCredits = listOf(
                        ArtistCreditMusicBrainzModel(
                            artist = aimerArtistMusicBrainzModel,
                            name = "Aimer",
                            joinPhrase = " with ",
                        ),
                        ArtistCreditMusicBrainzModel(
                            artist = ArtistMusicBrainzNetworkModel(
                                id = "4dfe9c0b-7602-4189-9919-151d493d1028",
                                name = "chelly",
                            ),
                            name = "chelly (EGOIST)",
                        ),
                    ),
                ),
                RecordingMusicBrainzNetworkModel(
                    id = "3c1234e3-13cb-4726-86ad-f7101fa3aef4",
                    name = "ninelie",
                    disambiguation = "TV size",
                    artistCredits = listOf(
                        ArtistCreditMusicBrainzModel(
                            artist = aimerArtistMusicBrainzModel,
                            name = "Aimer",
                            joinPhrase = " with ",
                        ),
                        ArtistCreditMusicBrainzModel(
                            artist = ArtistMusicBrainzNetworkModel(
                                id = "4dfe9c0b-7602-4189-9919-151d493d1028",
                                name = "chelly",
                            ),
                            name = "chelly (EGOIST)",
                        ),
                    ),
                ),
                RecordingMusicBrainzNetworkModel(
                    id = "1b401117-42ae-4771-bb96-7aa31025f766",
                    name = "春はゆく",
                    disambiguation = "",
                    artistCredits = listOf(
                        ArtistCreditMusicBrainzModel(
                            artist = aimerArtistMusicBrainzModel,
                            name = "Aimer",
                        ),
                    ),
                ),
            ),
        ).observeRecordings(
            browseMethod = BrowseMethod.ByEntity(
                entityId = aimerArtistMusicBrainzModel.id,
                entity = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
        ).asSnapshot()
        artistCollaborationRepositoryImpl.getAllCollaboratingArtistsAndEntities(
            artistId = aimerArtistMusicBrainzModel.id,
            collaborationEntityType = MusicBrainzEntityType.RECORDING,
            query = "",
        ).let { collaboratingArtistsAndEntities ->
            assertEquals(
                listOf(
                    CollaboratingArtistAndEntity(
                        artistId = "4dfe9c0b-7602-4189-9919-151d493d1028",
                        artistName = "chelly (EGOIST)",
                        entity = MusicBrainzEntityType.RECORDING,
                        entityId = "fea5134e-f0ee-49d5-a643-470fbc893241",
                        entityName = "ninelie",
                    ),
                    CollaboratingArtistAndEntity(
                        artistId = "4dfe9c0b-7602-4189-9919-151d493d1028",
                        artistName = "chelly (EGOIST)",
                        entity = MusicBrainzEntityType.RECORDING,
                        entityId = "3c1234e3-13cb-4726-86ad-f7101fa3aef4",
                        entityName = "ninelie (TV size)",
                    ),
                ),
                collaboratingArtistsAndEntities,
            )
        }
        artistCollaborationRepositoryImpl.getAllCollaboratingArtistsAndEntities(
            artistId = aimerArtistMusicBrainzModel.id,
            collaborationEntityType = MusicBrainzEntityType.RELEASE,
            query = "",
        ).let { collaboratingArtistsAndEntities ->
            assertEquals(
                emptyList<CollaboratingArtistAndEntity>(),
                collaboratingArtistsAndEntities,
            )
        }
        artistCollaborationRepositoryImpl.getAllCollaboratingArtistsAndEntities(
            artistId = aimerArtistMusicBrainzModel.id,
            collaborationEntityType = MusicBrainzEntityType.RELEASE_GROUP,
            query = "",
        ).let { collaboratingArtistsAndEntities ->
            assertEquals(
                emptyList<CollaboratingArtistAndEntity>(),
                collaboratingArtistsAndEntities,
            )
        }
    }
}
