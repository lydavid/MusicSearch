package ly.david.musicsearch.android.app.ui.collection

// TODO: maestro test instead
/**
 * Tests [CollectionList]-specific features.
 */
// internal class CollectionListScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val collectionDao: CollectionDao by inject()
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
//    fun createCollections() = runTest(timeout = 15.seconds) {
//        composeTestRule
//            .onNodeWithText(strings.collections)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.createCollection)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(strings.cancel)
//            .performClick()
//
//        val name1 = "My test collection"
//        composeTestRule
//            .onNodeWithContentDescription(strings.createCollection)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(strings.name)
//            .performClick()
//            .performTextInput(name1)
//        composeTestRule
//            .onNodeWithText(strings.ok)
//            .performClick()
//        composeTestRule
//            .onNodeWithText("My test collection")
//            .assertIsDisplayed()
//
//        val name2 = "My other test collection"
//        composeTestRule
//            .onNodeWithContentDescription(strings.createCollection)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(strings.name)
//            .performClick()
//            .performTextInput(name2)
//        composeTestRule
//            .onNodeWithText(strings.resource)
//            .performClick()
//        composeTestRule
//            .onNodeWithTag("ExposedDropdownMenu")
//            .performScrollToNode(hasTestTag(MusicBrainzEntity.WORK.resourceUri))
//        composeTestRule
//            .onNodeWithTag(MusicBrainzEntity.WORK.resourceUri)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(strings.ok)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(name1)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(name2)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun filterCollections() = runTest {
//        composeTestRule
//            .onNodeWithText(strings.collections)
//            .performClick()
//
//        val name1 = "should find me"
//        collectionDao.insertLocal(
//            CollectionListItemModel(
//                id = "1",
//                name = name1,
//                entity = MusicBrainzEntity.AREA,
//                isRemote = false,
//                entityCount = 0,
//            )
//        )
//
//        val name2 = "but not me"
//        collectionDao.insertLocal(
//            CollectionListItemModel(
//                id = "2",
//                name = name2,
//                entity = MusicBrainzEntity.RECORDING,
//                isRemote = false,
//                entityCount = 0,
//            )
//        )
//
//        waitForThenAssertIsDisplayed(name1)
//        waitForThenAssertIsDisplayed(name2)
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.filter)
//            .performClick()
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
//            .performTextInput("should")
//        waitForThenAssertIsDisplayed(name1)
//        composeTestRule
//            .onNodeWithText(name2)
//            .assertIsNotDisplayedOrDoesNotExist()
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.filter)
//            .performClick()
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
//            .performTextInput("something such that we show no results")
//        composeTestRule
//            .onAllNodesWithText(name1)
//            .assertCountEquals(0)
//        composeTestRule
//            .onAllNodesWithText(name2)
//            .assertCountEquals(0)
//    }
// }
