package ly.david.musicsearch.data.repository.release

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.releaseWith3CatalogNumbersWithSameLabel
import ly.david.data.test.virginMusicLabelMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.TestLabelRepository
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleasesByEntityRepositoryImplTest : KoinTest, TestLabelRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val database: Database by inject()
    private val artistReleaseDao: ArtistReleaseDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val recordingReleaseDao: RecordingReleaseDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    private val releaseCountryDao: ReleaseCountryDao by inject()
    override val labelDao: LabelDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()

    private fun createReleasesByEntityRepository(
        releases: List<ReleaseMusicBrainzModel>,
    ): ReleasesByEntityRepository {
        return ReleasesByEntityRepositoryImpl(
            artistReleaseDao = artistReleaseDao,
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseReleasesByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                    include: String,
                ) = BrowseReleasesResponse(
                    count = 1,
                    offset = 0,
                    musicBrainzModels = releases,
                )
            },
            recordingReleaseDao = recordingReleaseDao,
            releaseDao = releaseDao,
            releaseCountryDao = releaseCountryDao,
            releaseReleaseGroupDao = releaseReleaseGroupDao,
        )
    }

    @Test
    fun `releases by label - release with multiple catalog numbers, multiple cover arts`() = runTest {
        val labelRepository = createLabelRepository(
            musicBrainzModel = virginMusicLabelMusicBrainzModel,
        )
        val labelId = virginMusicLabelMusicBrainzModel.id
        labelRepository.lookupLabel(
            labelId = labelId,
            forceRefresh = false,
        )

        val sut = createReleasesByEntityRepository(
            releases = listOf(
                releaseWith3CatalogNumbersWithSameLabel,
            ),
        )

        database.mbid_imageQueries.insert(
            mbid = releaseWith3CatalogNumbersWithSameLabel.id,
            thumbnailUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
            largeUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-1200.jpg",
            types = persistentListOf(),
            comment = null,
        )
        database.mbid_imageQueries.insert(
            mbid = releaseWith3CatalogNumbersWithSameLabel.id,
            thumbnailUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/37564563886-250.jpg",
            largeUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/37564563886-1200.jpg",
            types = persistentListOf(),
            comment = null,
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = sut.observeReleasesByEntity(
            entityId = labelId,
            entity = MusicBrainzEntity.LABEL,
            listFilters = ListFilters(
                query = "ウタ",
            ),
        )
        val releases: List<ReleaseListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            1,
            releases.size,
        )
        val release: ReleaseListItemModel = releases[0]
        Assert.assertEquals(
            ReleaseListItemModel(
                id = "38650e8c-3c6b-431e-b10b-2cfb6db847d5",
                name = "ウタの歌 ONE PIECE FILM RED",
                disambiguation = "初回限定盤",
                date = "2022-08-10",
                barcode = "4988031519660",
                status = "Official",
                statusId = null,
                countryCode = "JP",
                packaging = "Jewel Case",
                packagingId = null,
                asin = "B0B392M9SC",
                quality = "normal",
                catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                coverArtArchive = CoverArtArchiveUiModel(count = 11),
                textRepresentation = TextRepresentationUiModel(script = "Jpan", language = "jpn"),
                imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                imageId = 1,
                formattedFormats = null,
                formattedTracks = null,
                formattedArtistCredits = "Ado",
                releaseCountryCount = 0,
                visited = false,
            ),
            release,
        )
    }

    @Test
    fun `load 100, then 200 releases`() = runTest {
        val labelRepository = createLabelRepository(
            musicBrainzModel = virginMusicLabelMusicBrainzModel,
        )
        val labelId = virginMusicLabelMusicBrainzModel.id
        labelRepository.lookupLabel(
            labelId = labelId,
            forceRefresh = false,
        )

        val sut = createReleasesByEntityRepository(
            releases = (1..200).map {
                releaseWith3CatalogNumbersWithSameLabel.copy(
                    id = it.toString(),
                )
            },
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = sut.observeReleasesByEntity(
            entityId = labelId,
            entity = MusicBrainzEntity.LABEL,
            listFilters = ListFilters(),
        )
        flow.asSnapshot().let { releases ->
            Assert.assertEquals(
                100,
                releases.size,
            )
        }

        flow.asSnapshot {
            scrollTo(99)
        }.let { releases ->
            Assert.assertEquals(
                200,
                releases.size,
            )
        }
    }
}
