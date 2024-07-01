package ly.david.musicsearch.android.app.ui.navigation

// @RunWith(Parameterized::class)
// internal class NavigateToEachEntityWithTitleTest(
//    private val entity: MusicBrainzEntity,
// ) : MainActivityTest() {
//
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters(name = "{0}")
//        fun data(): Collection<MusicBrainzEntity> {
//            return MusicBrainzEntity.values().filterNot {
//                it in listOf(MusicBrainzEntity.URL, MusicBrainzEntity.COLLECTION)
//            }
//        }
//    }
//
//    private val imageLoaderFactory: ImageLoaderFactory by inject()
//    private val circuit: Circuit by inject()
//    private lateinit var navController: NavHostController
//
//    @Before
//    fun setupApp() {
//        Coil.setImageLoader(imageLoaderFactory)
//        composeTestRule.activity.setContent {
//            navController = rememberNavController()
//            PreviewTheme {
//                CircuitCompositionLocals(circuit) {
//                    TopLevelScaffold(navController)
//                }
//            }
//        }
//    }
//
//    @Test
//    fun navigateToEachEntityScreenWithCustomTitle() = runTest {
//        composeTestRule.awaitIdle()
//
//        val title = entity.resourceUri
//        val entityId = entity.toFakeMusicBrainzModel().id
//
//        navController.goToEntityScreen(entity = entity, id = entityId, title = title)
//
//        composeTestRule
//            .onNodeWithText(title)
//            .assertIsDisplayed()
//    }
// }
