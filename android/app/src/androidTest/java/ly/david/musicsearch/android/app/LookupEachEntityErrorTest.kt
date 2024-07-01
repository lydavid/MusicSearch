package ly.david.musicsearch.android.app

// @RunWith(Parameterized::class)
// internal class LookupEachEntityErrorTest(
//    private val entity: MusicBrainzEntity,
// ) : MainActivityTest() {
//
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters(name = "{0}")
//        fun data(): Collection<MusicBrainzEntity> {
//            return MusicBrainzEntity.values().filterNot {
//                it in listOf(
//                    MusicBrainzEntity.URL,
//                    MusicBrainzEntity.COLLECTION,
//                )
//            }
//        }
//    }
//
//    private val strings: AppStrings by inject()
//    private val circuit: Circuit by inject()
//    private lateinit var navController: NavHostController
//
//    @Before
//    fun setupApp() {
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
//    fun lookupEachEntityError() = runTest {
//        composeTestRule.awaitIdle()
//
//        val entityId = "error"
//        navController.goToEntityScreen(
//            entity = entity,
//            id = entityId,
//        )
//
//        waitForThenAssertAtLeastOneIsDisplayed(strings.retry)
//
//        // TODO: in order to make retry actually work, we need to be able to fake out error
//        //  rather than pass an id that results in error
//    }
// }
