package ly.david.musicsearch.android.app.ui.search

/**
 * General UI test for search screen. For testing each resource, see [SearchEachEntityTest].
 */
// @RunWith(AndroidJUnit4::class)
// internal class SearchMusicBrainzScreenTest : MainActivityTest() {
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
//    // TODO: sometimes fails, seems to be because of Espresso
// //    @Test
// //    fun searchWithEmptyText_thenBack() {
// //        runBlocking { composeTestRule.awaitIdle() }
// //
// //        composeTestRule
// //            .onNodeWithText(searchLabel)
// //            .assert(hasText(""))
// //            .performImeAction()
// //
// //        composeTestRule
// //            .onNode(isDialog())
// //            .assertIsDisplayed()
// //
// //        composeTestRule
// //            .onNodeWithText(emptySearchWarning)
// //            .assertIsDisplayed()
// //
// //        Espresso.pressBack()
// //
// //        composeTestRule
// //            .onAllNodes(isDialog())
// //            .assertCountEquals(0)
// //    }
//
//    @Test
//    fun enterSearchText_thenClear() = runTest {
//        composeTestRule.awaitIdle()
//
//        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
//            .onNodeWithTag(SearchScreenTestTag.TEXT_FIELD.name)
//
//        searchFieldNode
//            .assert(hasText(""))
//            .performTextInput("Hello there")
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.clearSearch)
//            .assertIsDisplayed()
//            .performClick()
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.clearSearch)
//            .assertDoesNotExist()
//
//        searchFieldNode
//            .assert(hasText(""))
//    }
//
//    @Test
//    fun searchHistorySmokeTest() = runTest(timeout = 15.seconds) {
//        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
//            .onNodeWithTag(SearchScreenTestTag.TEXT_FIELD.name)
//
//        searchFieldNode.performTextInput("Some search text")
//        waitForThenPerformClickOn(MusicBrainzEntity.ARTIST.toFakeMusicBrainzModel().name!!)
//        composeTestRule
//            .onNodeWithText(MusicBrainzEntity.ARTIST.toFakeMusicBrainzModel().name!!)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithContentDescription(strings.back)
//            .performClick()
//        composeTestRule
//            .onNodeWithContentDescription(strings.clearSearch)
//            .performClick()
//
//        searchFieldNode.performTextInput("Some other search text")
//        waitForThenPerformClickOn(MusicBrainzEntity.ARTIST.toFakeMusicBrainzModel().name!!)
//        composeTestRule
//            .onNodeWithText(MusicBrainzEntity.ARTIST.toFakeMusicBrainzModel().name!!)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithContentDescription(strings.back)
//            .performClick()
//        composeTestRule
//            .onNodeWithContentDescription(strings.clearSearch)
//            .performClick()
//
//        // Search query shows up in search history
//        composeTestRule
//            .onNodeWithText("Some search text")
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText("Some other search text")
//            .assertIsDisplayed()
//
//        // Can delete single search history
//        composeTestRule
//            .onNodeWithText("Some search text")
//            .performTouchInput { swipeRight(startX = 0f, endX = 500f) }
//        composeTestRule
//            .onNodeWithText("Some search text")
//            .assertIsNotDisplayedOrDoesNotExist()
//
//        // Can delete all search history
//        composeTestRule
//            .onNodeWithContentDescription(strings.clearSearchHistory)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(strings.no)
//            .performClick()
//        composeTestRule
//            .onNodeWithText("Some other search text")
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithContentDescription(strings.clearSearchHistory)
//            .performClick()
//        composeTestRule.awaitIdle()
//        composeTestRule
//            .onNodeWithText(strings.yes)
//            .performClick()
//        composeTestRule
//            .onNodeWithText("Some other search text")
//            .assertIsNotDisplayedOrDoesNotExist()
//    }
// }
