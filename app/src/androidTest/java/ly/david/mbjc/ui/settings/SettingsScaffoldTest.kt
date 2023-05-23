package ly.david.mbjc.ui.settings

// TODO: https://github.com/googlecodelabs/android-datastore/issues/48
//  https://issuetracker.google.com/issues/203087070
//@HiltAndroidTest
//internal class SettingsScaffoldTest : MainActivityTest(), StringReferences {
//
//    @Inject
//    lateinit var appPreferences: AppPreferences
//
//    @Before
//    fun setupApp() {
//        hiltRule.inject()
//
//        composeTestRule.activity.setContent {
//            BaseTheme(
//                context = composeTestRule.activity,
//                darkTheme = appPreferences.shouldUseDarkColors()
//            ) {
//                SettingsScaffold()
//            }
//        }
//    }
//
//    @After
//    fun tearDown() {
//        // TODO: this actually deletes our non-test datastore too...
//        File(composeTestRule.activity.filesDir, "datastore").deleteRecursively()
//    }
//
//    // TODO: [low] verify the screen did change color
//    @Test
//    fun selectTheme() {
//        composeTestRule
//            .onNodeWithText(system)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(theme)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(light)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(light)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(theme)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(dark)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(dark)
//            .assertIsDisplayed()
//    }
//}
