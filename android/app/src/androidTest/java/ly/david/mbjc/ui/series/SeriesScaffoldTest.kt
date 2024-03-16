package ly.david.mbjc.ui.series

// internal class SeriesScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val seriesRepository: SeriesRepository by inject()
//
//    private fun setSeries(seriesMusicBrainzModel: SeriesMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                SeriesUi(seriesId = seriesMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setSeries(fakeSeries)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        seriesRepository.lookupSeries(fakeSeries.id)
//        setSeries(fakeSeries)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(fakeSeries.getNameWithDisambiguation())
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
//
//    @Test
//    fun hasRelations() = runTest {
//        setSeries(fakeSeries)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(fakeInstrument.relations?.first()?.area?.name!!)
//    }
// }
