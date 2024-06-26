package ly.david.musicsearch.android.app.ui.area

/**
 * This class should test anything in [AreaUi] that we would otherwise have to QA manually.
 *
 * However, try to refrain from testing the details of constituent composables such as its cards.
 * These should be tested in its own test class (screenshot tests). For now, previews will be enough.
 */
// internal class AreaScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val areaRepository: AreaRepository by inject()
//
//    private fun setArea(areaMusicBrainzModel: AreaMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                AreaUi(areaId = areaMusicBrainzModel.id)
//            }
//        }
//    }
//
//    // region General
//    @Test
//    fun firstTimeVisit() = runTest {
//        setArea(ontario)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit() = runTest {
//        areaRepository.lookupArea(ontario.id)
//        setArea(ontario)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        composeTestRule
//            .onNodeWithText(ontario.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(strings.places)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakePlace.name)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun hasRelations() = runTest {
//        setArea(ontario)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(canada.name)
//        waitForThenAssertIsDisplayed(toronto.name)
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.filter)
//            .performClick()
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
//            .performTextInput("something such that we show no results")
//        composeTestRule
//            .onAllNodesWithText(canada.name)
//            .assertCountEquals(0)
//        composeTestRule
//            .onAllNodesWithText(toronto.name)
//            .assertCountEquals(0)
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
//            .performTextClearance()
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
//            .performTextInput("tor")
//        waitForThenAssertIsDisplayed(toronto.name)
//        composeTestRule
//            .onNodeWithText(canada.name)
//            .assertIsNotDisplayedOrDoesNotExist()
//    }
//
//    @Test
//    fun nonCountryStatsExcludesReleases() = runTest {
//        setArea(ontario)
//
//        waitForThenPerformClickOn(strings.stats)
//
//        // Differentiate between Releases tab and header inside stats
//        composeTestRule
//            .onNode(hasText(strings.releases).and(hasNoClickAction()))
//            .assertDoesNotExist()
//    }
//
//    // TODO: visit, check history count is 1, visit again, go to release, return, return, check history count is 2
//
//    // endregion
//
//    // region Country
//    @Test
//    fun countryHasReleasesTab() = runTest {
//        setArea(canada)
//
//        waitForThenPerformClickOn(strings.releases)
//        waitForThenAssertIsDisplayed(underPressure.name)
//    }
//
//    @Test
//    fun countryStatsIncludesReleases() = runTest {
//        setArea(canada)
//
//        waitForThenPerformClickOn(strings.releases)
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.releases).and(hasNoClickAction()))
//    }
//    // endregion
// }
