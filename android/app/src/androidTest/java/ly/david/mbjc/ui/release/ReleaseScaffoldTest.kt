package ly.david.mbjc.ui.release

// internal class ReleaseScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val releaseRepository: ReleaseRepository by inject()
//    private val imageLoaderFactory: ImageLoaderFactory by inject()
//
//    @Before
//    fun setupApp() {
//        Coil.setImageLoader(imageLoaderFactory)
//    }
//
//    private fun setRelease(releaseMusicBrainzModel: ReleaseMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                ReleaseUi(releaseId = releaseMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest(timeout = 20.seconds) {
//        setRelease(underPressure)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest(timeout = 20.seconds) {
//        releaseRepository.lookupRelease(underPressure.id)
//        setRelease(underPressure)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        // Title
//        waitForThenAssertIsDisplayed(underPressure.name)
//
//        testDetailsTab()
// //        testTracksTab()
//        testRelationshipsTab()
//        testStatsTab()
//
//        // Confirm that up navigation items exists
//        waitForNodeToShow(hasTestTag("TopBarSubtitle"))
//        composeTestRule
//            .onNodeWithTag("TopBarSubtitle")
//            .performClick()
//        composeTestRule
//            .onNode(
//                matcher = hasText(underPressureReleaseGroup.name).and(
//                    hasAnySibling(hasText(queenArtistCredit.name))
//                )
//            )
//            .assertIsDisplayed()
//        waitForThenAssertIsDisplayed(queenArtistCredit.name)
//        waitForThenAssertIsDisplayed(davidBowieArtistCredit.name)
//    }
//
//    private fun testDetailsTab() {
//        waitForNodeToShow(hasTestTag(LargeImageTestTag.IMAGE.name))
//        composeTestRule
//            .onNodeWithTag(LargeImageTestTag.IMAGE.name)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithTag(LargeImageTestTag.IMAGE.name)
//            .performTouchInput {
//                swipeUp(startY = 500f, endY = 0f)
//            }
//        val barcode = "Barcode: ${underPressure.barcode!!}"
//        waitForThenAssertIsDisplayed(barcode)
//        waitForThenAssertIsDisplayed(underPressureLabelInfo.label!!.name)
//        waitForThenAssertIsDisplayed(underPressureLabelInfo.catalogNumber!!)
//        waitForThenAssertIsDisplayed(elektraMusicGroup.name)
//        waitForThenAssertIsDisplayed(fakeReleaseEvent.area!!.name)
//        waitForThenAssertIsDisplayed(fakeReleaseEvent.date!!)
//        composeTestRule
//            .onNodeWithTag(LargeImageTestTag.IMAGE.name)
//            .performTouchInput {
//                swipeDown(startY = 0f, endY = 500f)
//            }
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.filter)
//            .performClick()
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
//            .performTextInput("something such that we show no results")
//        // TODO: test filtering the other fields
//        composeTestRule
//            .onNodeWithText(barcode)
//            .assertIsNotDisplayedOrDoesNotExist()
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_BACK.name)
//            .performClick()
//    }
//
//    // TODO: always empty
//    private fun testTracksTab() {
//        waitForThenPerformClickOn(strings.tracks)
//        composeTestRule
//            .onNode(
//                matcher = hasText(underPressureTrack.title).and(
//                    hasAnySibling(hasText(underPressureTrack.number))
//                ),
//                useUnmergedTree = true
//            )
//            .assertIsDisplayed()
//        waitForThenAssertIsDisplayed(soulBrotherTrack.title)
//        // TODO: attempted to test filtering but apparently our listitem nodes gets duplicated afterwards...
//    }
//
//    private fun testRelationshipsTab() {
//        waitForThenPerformClickOn(strings.relationships)
//        composeTestRule
//            .onNode(
//                matcher = hasText(underPressure.name).and(
//                    hasAnySibling(hasText("${underPressureRemasterOf.getHeader()}:"))
//                ),
//                useUnmergedTree = true
//            )
//            .assertIsDisplayed()
//    }
//
//    private fun testStatsTab() {
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
// }
