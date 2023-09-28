package ly.david.data.room.label

//@RunWith(RobolectricTestRunner::class)
//internal class ReleaseLabelDaoTest : KoinTest {
//
//    private val releaseLabelDao: RoomReleaseLabelDao by inject()
//    private val releaseDao: RoomReleaseDao by inject()
//    private val labelDao: RoomLabelDao by inject()
//
//    @Before
//    fun setUp() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        startKoin {
//            modules(
//                roomDatabaseDaoModule,
//                testDatabaseModule,
//                module {
//                    single<Context> {
//                        context
//                    }
//                }
//            )
//        }
//    }
//
//    @After
//    fun tearDown() {
//        stopKoin()
//    }
//
//    @Test
//    fun `getNumberOfReleasesByLabel - only count distinct release & label pairs`() = runTest {
//        releaseDao.insert(
//            ReleaseRoomModel(
//                id = "release1",
//                name = "Release"
//            )
//        )
//        labelDao.insert(
//            LabelRoomModel(
//                id = "label1",
//                name = "Label",
//            )
//        )
//        releaseLabelDao.insertAll(
//            listOf(
//                ReleaseLabel(
//                    releaseId = "release1",
//                    labelId = "label1",
//                    catalogNumber = "cat1"
//                ),
//                ReleaseLabel(
//                    releaseId = "release1",
//                    labelId = "label1",
//                    catalogNumber = "cat2"
//                ),
//            )
//        )
//
//        assertThat(releaseLabelDao.getNumberOfReleasesByLabel("label1"), `is`(1))
//    }
//}
