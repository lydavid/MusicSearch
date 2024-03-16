package ly.david.mbjc.ui.releasegroup

// internal class ReleaseGroupScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val releaseGroupRepository: ReleaseGroupRepository by inject()
//    private val imageLoaderFactory: ImageLoaderFactory by inject()
//
//    @Before
//    fun setupApp() {
//        Coil.setImageLoader(imageLoaderFactory)
//    }
//
//    private fun setReleaseGroup(releaseGroupMusicBrainzModel: ReleaseGroupMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                ReleaseGroupUi(releaseGroupId = releaseGroupMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setReleaseGroup(underPressureReleaseGroup)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        releaseGroupRepository.lookupReleaseGroup(underPressureReleaseGroup.id)
//        setReleaseGroup(underPressureReleaseGroup)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(underPressureReleaseGroup.name)
//
//        // Confirm that up navigation items exists
//        waitForNodeToShow(hasTestTag("TopBarSubtitle"))
//        composeTestRule
//            .onNodeWithTag("TopBarSubtitle")
//            .performClick()
//
//        waitForThenAssertIsDisplayed(davidBowieArtistCredit.name)
//        waitForThenAssertIsDisplayed(queenArtistCredit.name)
//
//        waitForThenPerformClickOn(strings.releases)
//        composeTestRule
//            .onNode(
//                matcher = hasText(underPressure.name).and(hasAnySibling(hasText(underPressure.date!!))),
//                useUnmergedTree = true
//            )
//            .assertIsDisplayed()
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.releases).and(hasNoClickAction()))
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
//
//    @Test
//    fun hasRelations() = runTest {
//        setReleaseGroup(underPressureReleaseGroup)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(hotSpaceReleaseGroup.name)
//    }
// }
