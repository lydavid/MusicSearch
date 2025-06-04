package ly.david.musicsearch.data.repository.releasegroup

import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.repository.helpers.TestReleaseGroupRepository
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleaseGroupRepositoryImplTest : KoinTest, TestReleaseGroupRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createReleaseGroupRepository(
            musicBrainzModel = ReleaseGroupMusicBrainzNetworkModel(
                id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                name = "Today Is A Beautiful Day",
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "supercell (something)",
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                            name = "supercell",
                            sortName = "supercell",
                            type = "Group",
                            typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                            disambiguation = "j-pop",
                        ),
                        joinPhrase = "",
                    ),
                ),
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupReleaseGroup(
            releaseGroupId = "93bb79c2-2995-4607-af5e-061a25a4e06f",
            forceRefresh = false,
        )
        assertEquals(
            ReleaseGroupDetailsModel(
                id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                name = "Today Is A Beautiful Day",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                        name = "supercell (something)",
                        joinPhrase = "",
                    ),
                ),
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createReleaseGroupRepository(
            musicBrainzModel = ReleaseGroupMusicBrainzNetworkModel(
                id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                primaryType = "Album",
                name = "Today Is A Beautiful Day",
                firstReleaseDate = "2011-03-16",
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "supercell", // artist credit name changed
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                            name = "supercell",
                            sortName = "supercell",
                            type = "Group",
                            typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                            disambiguation = "j-pop",
                        ),
                        joinPhrase = "",
                    ),
                ),
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "official homepage",
                        typeId = "87d97dfc-3206-42fd-89d5-99593d5f1297",
                        targetType = SerializableMusicBrainzEntity.URL,
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "03620ecf-4ea0-4833-9cd7-2e9b73dd23bd",
                            resource = "http://www.supercell.jp/2nd/",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikidata",
                        typeId = "b988d08c-5d86-4a57-9557-c83b399e3580",
                        targetType = SerializableMusicBrainzEntity.URL,
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "c3e175ed-6618-42d9-8d0f-680883a35f43",
                            resource = "https://www.wikidata.org/wiki/Q1058017",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikipedia",
                        typeId = "6578f0e9-1ace-4095-9de8-6e517ddb1ceb",
                        targetType = SerializableMusicBrainzEntity.URL,
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "c6d64f5a-dcde-425c-b9d6-a84a74a805c3",
                            resource = "https://en.wikipedia.org/wiki/Today_Is_a_Beautiful_Day",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupReleaseGroup(
            releaseGroupId = "93bb79c2-2995-4607-af5e-061a25a4e06f",
            forceRefresh = false,
        )
        assertEquals(
            ReleaseGroupDetailsModel(
                id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                name = "Today Is A Beautiful Day",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                        name = "supercell (something)",
                        joinPhrase = "",
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupReleaseGroup(
            releaseGroupId = "93bb79c2-2995-4607-af5e-061a25a4e06f",
            forceRefresh = true,
        )
        assertEquals(
            ReleaseGroupDetailsModel(
                id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                primaryType = "Album",
                name = "Today Is A Beautiful Day",
                firstReleaseDate = "2011-03-16",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                        name = "supercell",
                        joinPhrase = "",
                    ),
                ),
                urls = listOf(
                    RelationListItemModel(
                        id = "c3e175ed-6618-42d9-8d0f-680883a35f43_4",
                        linkedEntityId = "c3e175ed-6618-42d9-8d0f-680883a35f43",
                        label = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q1058017",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "c6d64f5a-dcde-425c-b9d6-a84a74a805c3_5",
                        linkedEntityId = "c6d64f5a-dcde-425c-b9d6-a84a74a805c3",
                        label = "Wikipedia",
                        name = "https://en.wikipedia.org/wiki/Today_Is_a_Beautiful_Day",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "03620ecf-4ea0-4833-9cd7-2e9b73dd23bd_3",
                        linkedEntityId = "03620ecf-4ea0-4833-9cd7-2e9b73dd23bd",
                        label = "standalone website",
                        name = "http://www.supercell.jp/2nd/",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
