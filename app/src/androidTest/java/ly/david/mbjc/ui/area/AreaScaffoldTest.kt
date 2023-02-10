package ly.david.mbjc.ui.area

/**
 * This class should test anything in [AreaScaffold] that we would otherwise have to QA manually.
 *
 * However, try to refrain from testing the details of constituent composables such as its cards.
 * These should be tested in its own test class (screenshot tests). For now, previews will be enough.
 */
//@HiltAndroidTest
//internal class AreaScaffoldTest : MainActivityTestWithMockServer(), StringReferences {
//
//    @Inject
//    lateinit var relationDao: RelationDao
//
//    @Inject
//    lateinit var areaDao: AreaDao
//
//    private fun setArea(areaMusicBrainzModel: AreaMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                AreaScaffold(areaId = areaMusicBrainzModel.id)
//            }
//        }
//    }
//
//    // region General
//    @Test
//    fun firstTimeVisit() {
//        setArea(fakeArea)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit() {
//        setArea(fakeArea)
//        runBlocking {
//            areaDao.insert(fakeArea.toAreaRoomModel())
//            composeTestRule.awaitIdle()
//        }
//
//        assertFieldsDisplayed()
//    }
//
//    // TODO: flake
//    private fun assertFieldsDisplayed() {
//        composeTestRule
//            .onNodeWithText(fakeArea.name)
//            .assertIsDisplayed()
//
//        // TODO: can't differentiate between local/network
//        //  also maybe it's better to test this composables independently
//        composeTestRule
//            .onNodeWithText(places)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakePlace.name)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun areaHasRelations() {
//        setArea(fakeAreaWithRelation)
//
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(relationships)
//            .performClick()
//
//        // Relations are loaded
//        composeTestRule
//            .onNodeWithText(fakeAreaWithRelation.relations?.first()?.area?.name ?: "")
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun nonCountryStatsExcludesReleases() {
//        setArea(fakeArea)
//
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(stats)
//            .performClick()
//
//        // Need to differentiate between Releases tab and header inside stats
//        composeTestRule
//            .onNode(hasText(releases).and(hasNoClickAction()))
//            .assertDoesNotExist()
//    }
//
//    @Test
//    fun showRetryButtonOnError() {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                AreaScaffold(
//                    areaId = "error"
//                )
//            }
//        }
//
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(retry)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(relationships)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText(retry)
//            .assertIsDisplayed()
//
//        // TODO: showing "no results"
////        composeTestRule
////            .onNodeWithText(places)
////            .performClick()
////
////        composeTestRule
////            .onNodeWithText(retry)
////            .assertIsDisplayed()
//    }
//
//    // TODO: visit, check history count is 1, visit again, go to release, return, return, check history count is 2
//
//    // endregion
//
//    // region Country
//    @Test
//    fun countryHasReleasesTab() {
//        setArea(fakeCountry)
//
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(releases)
//            .performClick()
//
//        // Releases are loaded
//        composeTestRule
//            .onNodeWithText(fakeRelease.name)
//            .assertIsDisplayed()
//    }
//
////    @Test
////    fun countryStatsIncludesReleases() {
////        setArea(fakeCountry)
////
////        runBlocking { composeTestRule.awaitIdle() }
////
////        composeTestRule
////            .onNodeWithText(stats)
////            .performClick()
////
////        // Need to differentiate between Releases tab and header inside stats
////        composeTestRule
////            .onNode(hasText(releases).and(hasNoClickAction()), useUnmergedTree = true)
////            .assertIsDisplayed()
////    }
//    // endregion
//}
