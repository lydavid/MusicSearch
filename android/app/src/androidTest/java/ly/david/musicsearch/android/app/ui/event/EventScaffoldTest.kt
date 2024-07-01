package ly.david.musicsearch.android.app.ui.event

// internal class EventScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val eventRepository: EventRepository by inject()
//
//    private fun setEvent(eventMusicBrainzModel: EventMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                EventUi(eventId = eventMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setEvent(fakeEvent)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        eventRepository.lookupEvent(fakeEvent.id)
//        setEvent(fakeEvent)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(fakeEvent.getNameWithDisambiguation())
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
//
//    @Test
//    fun hasRelations() = runTest {
//        setEvent(fakeEvent)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertIsDisplayed(fakeEvent.relations?.first()?.area?.name!!)
//    }
// }
