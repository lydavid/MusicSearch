package ly.david.mbjc.ui.history

// internal class HistoryScreenTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val circuit: Circuit by inject()
//    private lateinit var navController: NavHostController
//
//    private val lookupHistoryRepository: LookupHistoryRepository by inject()
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
//    fun emptyLookupHistory() = runTest {
//        composeTestRule.awaitIdle()
//        navController.navigate(Destination.HISTORY.route)
//
//        composeTestRule
//            .onNodeWithText(strings.recentHistory)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(strings.noResultsFound)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun lookupHistoryWithAnItem() = runTest {
//        lookupHistoryRepository.upsert(lookupHistory)
//        composeTestRule.awaitIdle()
//        navController.navigate(Destination.HISTORY.route)
//
//        composeTestRule
//            .onNodeWithText(strings.recentHistory)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(lookupHistory.title)
//            .assertIsDisplayed()
//    }
// }
