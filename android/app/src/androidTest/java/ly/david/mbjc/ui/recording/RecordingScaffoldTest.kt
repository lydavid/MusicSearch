package ly.david.mbjc.ui.recording

// internal class RecordingScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val recordingRepository: RecordingRepository by inject()
//
//    private fun setRecording(recordingMusicBrainzModel: RecordingMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                RecordingUi(recordingId = recordingMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setRecording(underPressureRecording)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        recordingRepository.lookupRecording(underPressureRecording.id)
//        setRecording(underPressureRecording)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(underPressureRecording.name)
//
//        // Confirm that up navigation items exists
//        composeTestRule
//            .onNodeWithTag("TopBarSubtitle")
//            .performClick()
//        composeTestRule
//            .onNodeWithText(davidBowieArtistCredit.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(queenArtistCredit.name)
//            .assertIsDisplayed()
//
//        waitForThenPerformClickOn(strings.releases)
//        composeTestRule
//            .onNode(
//                matcher = hasText(underPressure.name).and(hasAnySibling(hasText(underPressure.date!!))),
//                useUnmergedTree = true
//            )
//            .assertIsDisplayed()
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(davidBowie.name)
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.releases).and(hasNoClickAction()))
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
// }
