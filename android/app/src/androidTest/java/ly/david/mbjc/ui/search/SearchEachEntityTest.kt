package ly.david.mbjc.ui.search

import ly.david.musicsearch.core.models.network.searchableEntities

/**
 * Test interacting with each [searchableEntities] from [SearchScreen].
 */
// @RunWith(Parameterized::class)
// internal class SearchEachEntityTest(
//    private val entity: MusicBrainzEntity,
// ) : MainActivityTest() {
//
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters(name = "{0}")
//        fun data(): Collection<MusicBrainzEntity> {
//            return searchableEntities
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
//    fun searchEachEntity() {
//        composeTestRule
//            .onNodeWithText(strings.resource)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithTag("ExposedDropdownMenu")
//            .performScrollToNode(hasTestTag(entity.resourceUri))
//        composeTestRule
//            .onNodeWithTag(entity.resourceUri)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText(entity.getDisplayText(strings))
//            .assertIsDisplayed()
//
//        // Entity shows up in search result
//        composeTestRule.onRoot().printToLog("MY TAG")
//
//        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
//            .onNodeWithTag(SearchScreenTestTag.TEXT_FIELD.name)
//
//        searchFieldNode.assert(hasText(""))
//            .performTextInput("Some search text")
//        waitForThenPerformClickOn(entity.toFakeMusicBrainzModel().name!!)
//        composeTestRule
//            .onNodeWithText(entity.toFakeMusicBrainzModel().name!!)
//            .assertIsDisplayed()
//
//        // Entity shows up in history
//        composeTestRule
//            .onNodeWithText(strings.history)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(entity.toFakeMusicBrainzModel().name!!)
//            .assertIsDisplayed()
//            .performClick()
//        composeTestRule
//            .onNodeWithText(entity.toFakeMusicBrainzModel().name!!)
//            .assertIsDisplayed()
//    }
// }
