package ly.david.mbjc.ui.instrument

// internal class InstrumentScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val instrumentRepository: InstrumentRepository by inject()
//
//    private fun setInstrument(instrumentMusicBrainzModel: InstrumentMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                InstrumentUi(entityId = instrumentMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setInstrument(fakeInstrument)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        instrumentRepository.lookupInstrument(fakeInstrument.id)
//        setInstrument(fakeInstrument)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(fakeInstrument.getNameWithDisambiguation())
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
//
//    @Test
//    fun hasRelations() = runTest {
//        setInstrument(fakeInstrument)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(fakeInstrument.relations?.first()?.area?.name!!)
//    }
// }
