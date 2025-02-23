package ly.david.musicsearch.data.repository.release

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CoverArtArchiveMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseEventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.TextRepresentationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleaseRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val releaseDao: ReleaseDao by inject()
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    private val releaseGroupDao: ReleaseGroupDao by inject()
    private val artistCreditDao: ArtistCreditDao by inject()
    private val releaseCountryDao: ReleaseCountryDao by inject()
    private val areaDao: AreaDao by inject()
    private val labelDao: LabelDao by inject()
    private val releaseLabelDao: ReleaseLabelDao by inject()
    private val mediumDao: MediumDao by inject()
    private val trackDao: TrackDao by inject()
    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val visitedDao: VisitedDao by inject()
    private val relationDao: RelationDao by inject()

    private fun createRepositoryWithFakeNetworkData(
        musicBrainzModel: ReleaseMusicBrainzModel,
    ): ReleaseRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRelease(
                    releaseId: String,
                    include: String,
                ): ReleaseMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return ReleaseRepositoryImpl(
            releaseDao = releaseDao,
            releaseReleaseGroupDao = releaseReleaseGroupDao,
            releaseGroupDao = releaseGroupDao,
            artistCreditDao = artistCreditDao,
            releaseCountryDao = releaseCountryDao,
            areaDao = areaDao,
            labelDao = labelDao,
            releaseLabelDao = releaseLabelDao,
            relationRepository = relationRepository,
            mediumDao = mediumDao,
            trackDao = trackDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRelease(
                    releaseId: String,
                    include: String,
                ): ReleaseMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createRepositoryWithFakeNetworkData(
            musicBrainzModel = ReleaseMusicBrainzModel(
                id = "8516ca87-f9c4-3854-a727-6d328cf44837",
                name = "Today Is A Beautiful Day",
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "supercell",
                        artist = ArtistMusicBrainzModel(
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
                releaseGroup = ReleaseGroupMusicBrainzModel(
                    id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                    primaryType = "Album",
                    name = "Today Is A Beautiful Day",
                    firstReleaseDate = "2011-03-16",
                ),
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupRelease(
            releaseId = "8516ca87-f9c4-3854-a727-6d328cf44837",
            forceRefresh = false,
        )
        assertEquals(
            ReleaseDetailsModel(
                id = "8516ca87-f9c4-3854-a727-6d328cf44837",
                name = "Today Is A Beautiful Day",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                        name = "supercell",
                        joinPhrase = "",
                    ),
                ),
                releaseGroup = ReleaseGroupForRelease(
                    id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                    primaryType = "Album",
                    name = "Today Is A Beautiful Day",
                    firstReleaseDate = "2011-03-16",
                ),
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createRepositoryWithFakeNetworkData(
            musicBrainzModel = ReleaseMusicBrainzModel(
                id = "8516ca87-f9c4-3854-a727-6d328cf44837",
                name = "Today Is A Beautiful Day",
                disambiguation = "初回生産限定盤",
                date = "2011-03-16",
                status = "Official",
                statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
                barcode = "4988009047348",
                countryCode = "JP",
                packaging = "Jewel Case",
                packagingId = "ec27701a-4a22-37f4-bfac-6616e0f9750a",
                asin = "B004GJ33BO",
                quality = "normal",
                coverArtArchive = CoverArtArchiveMusicBrainzModel(
                    count = 21,
                ),
                textRepresentation = TextRepresentationMusicBrainzModel(
                    script = "Jpan",
                    language = "jpn",
                ),
                releaseEvents = listOf(
                    ReleaseEventMusicBrainzModel(
                        area = AreaMusicBrainzModel(
                            id = "2db42837-c832-3c27-b4a3-08198f75693c",
                            name = "Japan",
                            sortName = "Japan",
                            countryCodes = listOf("JP"),
                        ),
                        date = "2011-03-16",
                    ),
                ),
                media = listOf(
                    MediumMusicBrainzModel(
                        position = 1,
                        formatId = "9712d52a-4509-3d4b-a1a2-67c88c643e31",
                        format = "CD",
                        trackCount = 13,
                        tracks = listOf(
                            TrackMusicBrainzModel(
                                id = "c9700f84-638f-3dec-9170-a15eb4f0cc96",
                                position = 1,
                                number = "1",
                                title = "終わりへ向かう始まりの歌",
                                length = 130000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
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
                                recording = RecordingMusicBrainzModel(
                                    id = "ff84b4f1-7879-4936-8b62-35dc2c8a098a",
                                    name = "終わりへ向かう始まりの歌",
                                    length = 130000,
                                    firstReleaseDate = "2011-03-16",
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "7f1cb4f5-5936-3a51-a08d-6bf715a237d0",
                                position = 2,
                                number = "2",
                                title = "君の知らない物語",
                                length = 339000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
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
                                recording = RecordingMusicBrainzModel(
                                    id = "1c8d2ed4-258f-4435-8db9-fa2f8be24712",
                                    name = "君の知らない物語",
                                    length = 341053,
                                    firstReleaseDate = "2009-08-12",
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "a941e1a2-79de-33c9-a02b-a7f067e13aef",
                                position = 3,
                                number = "3",
                                title = "ヒーロー",
                                length = 311000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "be5911d7-b349-49d7-b1de-d57fc27d0977",
                                    name = "ヒーロー",
                                    length = 311000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "bf7143f7-f93a-3731-bc7d-c0ff84728ca6",
                                position = 4,
                                number = "4",
                                title = "Perfect Day",
                                length = 286000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "66512715-98f8-49d1-8df8-3fd80e5da2de",
                                    name = "Perfect Day",
                                    length = 286000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "066266e5-b58b-3e71-91db-5ad2a8ed2532",
                                position = 5,
                                number = "5",
                                title = "復讐",
                                length = 203000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "803f742f-6f81-4674-99bb-73ce768613c5",
                                    name = "復讐",
                                    length = 203000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "79a09526-fb42-3bf8-a85b-e788fcbfabf6",
                                position = 6,
                                number = "6",
                                title = "ロックンロールなんですの",
                                length = 216000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "a7875312-f3e4-4c8c-9029-c77c6e623acf",
                                    name = "ロックンロールなんですの",
                                    length = 216000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "441bf9d5-b91a-36ea-9f15-56317ab22676",
                                position = 7,
                                number = "7",
                                title = "LOVE & ROLL",
                                length = 295000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "824c4fcd-a405-4faa-be72-82a01753d18e",
                                    name = "LOVE & ROLL",
                                    length = 295000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "b38da0ab-d752-39b4-9517-d17a33aa4cc5",
                                position = 8,
                                number = "8",
                                title = "Feel so good",
                                length = 301000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "69ca2c36-8642-4968-9e7d-451d9d98114c",
                                    name = "Feel so good",
                                    length = 301000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "a58a39b1-93ba-3137-906d-4210255f19a1",
                                position = 9,
                                number = "9",
                                title = "星が瞬くこんな夜に",
                                length = 269000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "c943aa35-24ad-4859-9f2f-ab6e218974bf",
                                    name = "星が瞬くこんな夜に",
                                    length = 269000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "961ef5f0-197f-3a5c-9c81-198264a59cf6",
                                position = 10,
                                number = "10",
                                title = "うたかた花火",
                                length = 360000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "bf20591e-cf4b-4a0a-8b88-bc1ab6c92d8f",
                                    name = "うたかた花火",
                                    length = 360000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "44adf604-f8e7-38ad-ad8b-fb58d50fac0a",
                                position = 11,
                                number = "11",
                                title = "夜が明けるよ",
                                length = 290000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "10c376ec-6f7f-44c6-b4b2-32f73e7bf816",
                                    name = "夜が明けるよ",
                                    length = 290000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "2622e69b-d15a-3acd-a2ad-417bc3238f40",
                                position = 12,
                                number = "12",
                                title = "さよならメモリーズ",
                                length = 368000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "0ef04153-cd64-4d38-bd58-8a469dae7664",
                                    name = "さよならメモリーズ",
                                    length = 367000,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "1cd30e11-c4d6-3281-81cd-623043ea936e",
                                position = 13,
                                number = "13",
                                title = "私へ",
                                length = 126000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "4714901e-d0e4-4121-900b-bc95043580a3",
                                    name = "私へ",
                                    length = 126000,
                                ),
                            ),
                        ),
                    ),
                    MediumMusicBrainzModel(
                        position = 2,
                        formatId = "bb71fd58-ff93-32b4-a201-4ad1b2a80e5f",
                        format = "DVD-Video",
                        trackCount = 5,
                        tracks = listOf(
                            TrackMusicBrainzModel(
                                id = "1af38b9b-5d34-3350-98ba-33276886b3be",
                                position = 1,
                                number = "1",
                                title = "「君の知らない物語」 ×「君化物語」 コラボCM",
                                length = 16000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                            disambiguation = "j-pop",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "7587430d-910c-48dc-9dc1-d7d7061c5b01",
                                    name = "「君の知らない物語」 ×「君化物語」 コラボCM",
                                    length = 16000,
                                    video = true,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "04f225b8-a641-3adf-b41f-8f50db310759",
                                position = 2,
                                number = "2",
                                title = "「センコロールトレーラー映像",
                                length = 85000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                            disambiguation = "j-pop",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "a4c9ce09-30cf-46e8-af86-eabcb1011c74",
                                    name = "「センコロールトレーラー映像",
                                    length = 85000,
                                    video = true,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "40cfb01b-1f7a-332c-b9d6-a9a89bb059f8",
                                position = 3,
                                number = "3",
                                title = "「魔法使いの夜」トレーラー映像",
                                length = 217000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                            disambiguation = "j-pop",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "45fa775f-bd06-4f66-92f5-2511f1609e5b",
                                    name = "「魔法使いの夜」トレーラー映像",
                                    length = 217000,
                                    video = true,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "b0f703bf-2aa5-30aa-a4d7-c3827366e580",
                                position = 4,
                                number = "4",
                                title = "「アオハル」トレーラー映像",
                                length = 47000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                            disambiguation = "j-pop",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "06772c49-9d19-43d9-8012-79b3c3add6d5",
                                    name = "「アオハル」トレーラー映像",
                                    length = 47000,
                                    video = true,
                                ),
                            ),
                            TrackMusicBrainzModel(
                                id = "b91bf6db-7368-395e-8b26-5a2c77802228",
                                position = 5,
                                number = "5",
                                title = "「Perfect Day」Music Clip",
                                length = 297000,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        name = "supercell",
                                        artist = ArtistMusicBrainzModel(
                                            id = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                                            name = "supercell",
                                            sortName = "supercell",
                                            disambiguation = "j-pop",
                                        ),
                                    ),
                                ),
                                recording = RecordingMusicBrainzModel(
                                    id = "946f6d26-a685-4ea6-8d3e-9b1c5996d063",
                                    name = "「Perfect Day」Music Clip",
                                    length = 297000,
                                    video = true,
                                ),
                            ),
                        ),
                    ),
                ),
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "supercell",
                        artist = ArtistMusicBrainzModel(
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
                labelInfoList = listOf(
                    LabelInfo(
                        catalogNumber = "SRCL-7486",
                        label = LabelMusicBrainzModel(
                            id = "dee62e1a-cfd1-466f-b578-846a0fdf435a",
                            name = "Sony Records",
                            type = "Imprint",
                            typeId = "b6285b2a-3514-3d43-80df-fcf528824ded",
                            disambiguation = "Japanese imprint",
                        ),
                    ),
                    LabelInfo(
                        catalogNumber = "SRCL-7487",
                        label = LabelMusicBrainzModel(
                            id = "dee62e1a-cfd1-466f-b578-846a0fdf435a",
                            name = "Sony Records",
                            type = "Imprint",
                            typeId = "b6285b2a-3514-3d43-80df-fcf528824ded",
                            disambiguation = "Japanese imprint",
                        ),
                    ),
                ),
                releaseGroup = ReleaseGroupMusicBrainzModel(
                    id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                    primaryType = "Album",
                    name = "Today Is A Beautiful Day",
                    firstReleaseDate = "2011-03-16",
                ),
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "amazon asin",
                        typeId = "4f2e710d-166c-480c-a293-2e2c8d658d87",
                        targetType = SerializableMusicBrainzEntity.URL,
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "8248e638-ee4d-4e03-a6c3-ba4ad7af00c1",
                            resource = "https://www.amazon.co.jp/gp/product/B004GJ33BO",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "discogs",
                        typeId = "4a78823c-1c53-4176-a5f3-58026c76f2bc",
                        targetType = SerializableMusicBrainzEntity.URL,
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "6f72b1ec-2634-4c23-a912-6e262ace69ea",
                            resource = "https://www.discogs.com/release/2899823",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "other databases",
                        typeId = "c74dee45-3c85-41e9-a804-92ab1c654446",
                        targetType = SerializableMusicBrainzEntity.URL,
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "c7ee5a1d-c11d-4511-809f-f5ba138a38d3",
                            resource = "https://rateyourmusic.com/release/album/supercell_f2/today_is_a_beautiful_day_f1/",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "vgmdb",
                        typeId = "6af0134a-df6a-425a-96e2-895f9cd342ba",
                        targetType = SerializableMusicBrainzEntity.URL,
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "9093d520-297d-4811-addb-93804d0723b8",
                            resource = "https://vgmdb.net/album/28417",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupRelease(
            releaseId = "8516ca87-f9c4-3854-a727-6d328cf44837",
            forceRefresh = false,
        )
        assertEquals(
            ReleaseDetailsModel(
                id = "8516ca87-f9c4-3854-a727-6d328cf44837",
                name = "Today Is A Beautiful Day",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                        name = "supercell",
                        joinPhrase = "",
                    ),
                ),
                releaseGroup = ReleaseGroupForRelease(
                    id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                    primaryType = "Album",
                    name = "Today Is A Beautiful Day",
                    firstReleaseDate = "2011-03-16",
                ),
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupRelease(
            releaseId = "8516ca87-f9c4-3854-a727-6d328cf44837",
            forceRefresh = true,
        )
        assertEquals(
            ReleaseDetailsModel(
                id = "8516ca87-f9c4-3854-a727-6d328cf44837",
                name = "Today Is A Beautiful Day",
                disambiguation = "初回生産限定盤",
                date = "2011-03-16",
                barcode = "4988009047348",
                status = "Official",
                statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
                countryCode = "JP",
                packaging = "Jewel Case",
                packagingId = "ec27701a-4a22-37f4-bfac-6616e0f9750a",
                asin = "B004GJ33BO",
                quality = "normal",
                coverArtArchive = CoverArtArchiveUiModel(count = 21),
                textRepresentation = TextRepresentationUiModel(
                    script = "Jpan",
                    language = "jpn",
                ),
                formattedFormats = "CD + DVD-Video",
                formattedTracks = "13 + 5",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "9b15ff5e-5bd1-43c2-821d-e31240aad334",
                        name = "supercell",
                        joinPhrase = "",
                    ),
                ),
                releaseGroup = ReleaseGroupForRelease(
                    id = "93bb79c2-2995-4607-af5e-061a25a4e06f",
                    primaryType = "Album",
                    name = "Today Is A Beautiful Day",
                    firstReleaseDate = "2011-03-16",
                ),
                areas = listOf(
                    AreaListItemModel(
                        id = "2db42837-c832-3c27-b4a3-08198f75693c",
                        name = "Japan",
                        countryCodes = listOf("JP"),
                        date = "2011-03-16",
                    ),
                ),
                labels = listOf(
                    LabelListItemModel(
                        id = "dee62e1a-cfd1-466f-b578-846a0fdf435a",
                        name = "Sony Records",
                        disambiguation = "Japanese imprint",
                        type = "Imprint",
                        labelCode = null,
                        catalogNumber = "SRCL-7486",
                    ),
                    LabelListItemModel(
                        id = "dee62e1a-cfd1-466f-b578-846a0fdf435a",
                        name = "Sony Records",
                        disambiguation = "Japanese imprint",
                        type = "Imprint",
                        labelCode = null,
                        catalogNumber = "SRCL-7487",
                    ),
                ),
                urls = listOf(
                    RelationListItemModel(
                        id = "8248e638-ee4d-4e03-a6c3-ba4ad7af00c1_4",
                        linkedEntityId = "8248e638-ee4d-4e03-a6c3-ba4ad7af00c1",
                        label = "ASIN",
                        name = "https://www.amazon.co.jp/gp/product/B004GJ33BO",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "6f72b1ec-2634-4c23-a912-6e262ace69ea_5",
                        linkedEntityId = "6f72b1ec-2634-4c23-a912-6e262ace69ea",
                        label = "Discogs",
                        name = "https://www.discogs.com/release/2899823",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "9093d520-297d-4811-addb-93804d0723b8_7",
                        linkedEntityId = "9093d520-297d-4811-addb-93804d0723b8",
                        label = "VGMdb",
                        name = "https://vgmdb.net/album/28417",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "c7ee5a1d-c11d-4511-809f-f5ba138a38d3_6",
                        linkedEntityId = "c7ee5a1d-c11d-4511-809f-f5ba138a38d3",
                        label = "other databases",
                        name = "https://rateyourmusic.com/release/album/supercell_f2/today_is_a_beautiful_day_f1/",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
                releaseLength = 4156000,
                hasNullLength = false,
            ),
            allDataArtistDetailsModel,
        )
    }

    private fun createRepositoryWithMedia(
        media: List<MediumMusicBrainzModel>?,
    ): ReleaseRepository {
        return createRepositoryWithFakeNetworkData(
            musicBrainzModel = ReleaseMusicBrainzModel(
                id = "f7a96d7b-67a7-4bc6-89dc-2a426f51b1f0",
                name = "真・女神転生30th Anniversary Special Sound Compilation",
                date = "2023-07-26",
                status = "Official",
                statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
                barcode = "4573471821543",
                countryCode = "JP",
                packaging = "Box",
                packagingId = "c1668fc7-8944-4a00-bc3e-46e8d861d211",
                asin = null,
                quality = "normal",
                coverArtArchive = CoverArtArchiveMusicBrainzModel(
                    count = 26,
                ),
                textRepresentation = TextRepresentationMusicBrainzModel(
                    script = "Jpan",
                    language = "jpn",
                ),
                releaseEvents = listOf(
                    ReleaseEventMusicBrainzModel(
                        area = AreaMusicBrainzModel(
                            id = "2db42837-c832-3c27-b4a3-08198f75693c",
                            name = "Japan",
                            sortName = "Japan",
                            countryCodes = listOf("JP"),
                        ),
                        date = "2023-07-26",
                    ),
                ),
                media = media,
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "アトラスサウンドチーム",
                        artist = ArtistMusicBrainzModel(
                            id = "37e85ee8-366a-4f17-a011-de94b6632408",
                            name = "アトラスサウンドチーム",
                            sortName = "ATLUS Sound Team",
                            type = "Group",
                            typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                            disambiguation = "",
                        ),
                        joinPhrase = "",
                    ),
                ),
                releaseGroup = ReleaseGroupMusicBrainzModel(
                    id = "a5a83577-ddca-4428-bf6b-b852296bc5f3",
                    primaryType = "Album",
                    secondaryTypes = listOf(
                        "Compilation",
                        "Soundtrack",
                    ),
                    name = "真・女神転生30th Anniversary Special Sound Compilation",
                    firstReleaseDate = "2011-03-16",
                ),
            ),
        )
    }

    @Test
    fun `refresh release tracks after changing artist credits`() = runTest {
        val releaseId = "f7a96d7b-67a7-4bc6-89dc-2a426f51b1f0"
        val releaseRepositoryBeforeEdit = createRepositoryWithMedia(
            listOf(
                MediumMusicBrainzModel(
                    position = 1,
                    formatId = "9712d52a-4509-3d4b-a1a2-67c88c643e31",
                    format = "CD",
                    trackCount = 1,
                    title = "SFC版「真・女神転生」",
                    tracks = listOf(
                        TrackMusicBrainzModel(
                            id = "c9700f84-638f-3dec-9170-a15eb4f0cc96",
                            position = 1,
                            number = "1",
                            title = "Demo",
                            length = 18733,
                            artistCredits = listOf(
                                ArtistCreditMusicBrainzModel(
                                    name = "アトラスサウンドチーム",
                                    artist = ArtistMusicBrainzModel(
                                        id = "37e85ee8-366a-4f17-a011-de94b6632408",
                                        name = "アトラスサウンドチーム",
                                        sortName = "ATLUS Sound Team",
                                        type = "Group",
                                        typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                                        disambiguation = "",
                                    ),
                                    joinPhrase = "",
                                ),
                            ),
                            recording = RecordingMusicBrainzModel(
                                id = "994b2961-3527-43f7-830d-7c817d286577",
                                name = "Demo",
                                length = 18733,
                                firstReleaseDate = "2023-07-26",
                            ),
                        ),
                    ),
                ),
            ),
        )
        val artistDetailsModelBeforeEdit = releaseRepositoryBeforeEdit.lookupRelease(
            releaseId = releaseId,
            forceRefresh = false,
        )
        assertEquals(
            ReleaseDetailsModel(
                id = releaseId,
                name = "真・女神転生30th Anniversary Special Sound Compilation",
                disambiguation = "",
                date = "2023-07-26",
                barcode = "4573471821543",
                status = "Official",
                statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
                countryCode = "JP",
                packaging = "Box",
                packagingId = "c1668fc7-8944-4a00-bc3e-46e8d861d211",
                asin = null,
                quality = "normal",
                coverArtArchive = CoverArtArchiveUiModel(count = 26),
                textRepresentation = TextRepresentationUiModel(
                    script = "Jpan",
                    language = "jpn",
                ),
                formattedFormats = "CD",
                formattedTracks = "1",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "37e85ee8-366a-4f17-a011-de94b6632408",
                        name = "アトラスサウンドチーム",
                        joinPhrase = "",
                    ),
                ),
                releaseGroup = ReleaseGroupForRelease(
                    id = "a5a83577-ddca-4428-bf6b-b852296bc5f3",
                    name = "真・女神転生30th Anniversary Special Sound Compilation",
                    firstReleaseDate = "2011-03-16",
                    disambiguation = "",
                    primaryType = "Album",
                    secondaryTypes = listOf(
                        "Compilation",
                        "Soundtrack",
                    ),
                ),
                areas = listOf(
                    AreaListItemModel(
                        id = "2db42837-c832-3c27-b4a3-08198f75693c",
                        name = "Japan",
                        sortName = "",
                        disambiguation = null,
                        type = null,
                        countryCodes = listOf("JP"),
                        date = "2023-07-26",
                        visited = false,
                    ),
                ),
                labels = emptyList(),
                urls = emptyList(),
                releaseLength = 18733,
                hasNullLength = false,
            ),
            artistDetailsModelBeforeEdit,
        )
        val tracksFlowBeforeEdit = releaseRepositoryBeforeEdit.observeTracksByRelease(
            releaseId,
            "",
        )
        val listItemModelsBeforeEdit: List<ListItemModel> = tracksFlowBeforeEdit.asSnapshot()
        assertEquals(
            listOf(
                ListSeparator(
                    id = "1",
                    text = "CD 1 (SFC版「真・女神転生」)",
                ),
                TrackListItemModel(
                    id = "c9700f84-638f-3dec-9170-a15eb4f0cc96",
                    position = 1,
                    number = "1",
                    title = "Demo",
                    length = 18733,
                    mediumId = 1,
                    recordingId = "994b2961-3527-43f7-830d-7c817d286577",
                    formattedArtistCredits = "アトラスサウンドチーム",
                    visited = false,
                    mediumPosition = 1,
                    mediumName = "SFC版「真・女神転生」",
                    trackCount = 1,
                    format = "CD",
                ),
            ),
            listItemModelsBeforeEdit,
        )

        val releaseRepositoryAfterEdit = createRepositoryWithMedia(
            media = listOf(
                MediumMusicBrainzModel(
                    position = 1,
                    formatId = "9712d52a-4509-3d4b-a1a2-67c88c643e31",
                    format = "CD",
                    trackCount = 1,
                    title = "SFC版「真・女神転生」",
                    tracks = listOf(
                        TrackMusicBrainzModel(
                            id = "c9700f84-638f-3dec-9170-a15eb4f0cc96",
                            position = 1,
                            number = "1",
                            title = "Demo",
                            length = 18733,
                            artistCredits = listOf(
                                ArtistCreditMusicBrainzModel(
                                    name = "増子司",
                                    artist = ArtistMusicBrainzModel(
                                        id = "ff3c73e4-234e-41ba-8000-6948a2d0fd6d",
                                        name = "増子司",
                                        sortName = "Masuko, Tsukasa",
                                        type = "Person",
                                        typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                                        disambiguation = "video game music composer",
                                    ),
                                    joinPhrase = "",
                                ),
                            ),
                            recording = RecordingMusicBrainzModel(
                                id = "994b2961-3527-43f7-830d-7c817d286577",
                                name = "Demo",
                                length = 18733,
                                firstReleaseDate = "2023-07-26",
                            ),
                        ),
                    ),
                ),
            ),
        )
        val tracksFlowAfterEdit = releaseRepositoryAfterEdit.observeTracksByRelease(
            releaseId,
            "",
        )
        val listItemModelsAfterEdit: List<ListItemModel> = tracksFlowAfterEdit.asSnapshot {
            refresh()
        }
        assertEquals(
            listOf(
                ListSeparator(
                    id = "2",
                    text = "CD 1 (SFC版「真・女神転生」)",
                ),
                TrackListItemModel(
                    id = "c9700f84-638f-3dec-9170-a15eb4f0cc96",
                    position = 1,
                    number = "1",
                    title = "Demo",
                    length = 18733,
                    mediumId = 2,
                    recordingId = "994b2961-3527-43f7-830d-7c817d286577",
                    formattedArtistCredits = "増子司",
                    visited = false,
                    mediumPosition = 1,
                    mediumName = "SFC版「真・女神転生」",
                    trackCount = 1,
                    format = "CD",
                ),
            ),
            listItemModelsAfterEdit,
        )
    }
}
