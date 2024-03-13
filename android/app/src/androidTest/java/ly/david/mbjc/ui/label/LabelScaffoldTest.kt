package ly.david.mbjc.ui.label

// internal class LabelScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val labelRepository: LabelRepository by inject()
//
//    private fun setLabel(labelMusicBrainzModel: LabelMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                LabelUi(labelId = labelMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setLabel(elektra)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        labelRepository.lookupLabel(elektra.id)
//        setLabel(elektra)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(elektra.getNameWithDisambiguation())
//
//        waitForThenPerformClickOn(strings.releases)
//        waitForThenAssertIsDisplayed(underPressure.name)
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.releases).and(hasNoClickAction()))
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
//
//    @Test
//    fun hasRelations() = runTest {
//        setLabel(elektra)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(elektraMusicGroup.name)
//    }
// }
