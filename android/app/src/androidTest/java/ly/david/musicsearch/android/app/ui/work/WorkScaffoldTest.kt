package ly.david.musicsearch.android.app.ui.work

// internal class WorkScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val repository: WorkRepository by inject()
//
//    private fun setWork(workMusicBrainzModel: WorkMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                WorkUi(workId = workMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setWork(fakeWorkWithAllData)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        repository.lookupWork(fakeWorkWithAllData.id)
//        setWork(fakeWorkWithAllData)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(fakeWorkWithAllData.getNameWithDisambiguation())
//        waitForThenAssertIsDisplayed("Type: ${fakeWorkWithAllData.type!!}")
//
//        val attributeType = fakeWorkWithAllData.attributes!!.first().type
//        val attributeValue = fakeWorkWithAllData.attributes!!.first().value
//        composeTestRule
//            .onNodeWithText("$attributeType: $attributeValue")
//            .assertIsDisplayed()
//
//        waitForThenPerformClickOn(strings.recordings)
//        waitForThenAssertIsDisplayed(underPressureRecording.name)
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.recordings).and(hasNoClickAction()))
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
//
//    // TODO: why does this take so long on CI? ~20s. is relations query really inefficient?
//    @Test
//    fun hasRelations() = runTest {
//        setWork(fakeWorkWithAllData)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(fakeWorkWithAllData.relations?.first()?.work?.name!!)
//    }
// }
