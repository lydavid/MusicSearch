package ly.david.mbjc.ui.settings

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import java.io.File
import javax.inject.Inject
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.BaseTheme
import org.junit.After
import org.junit.Before
import org.junit.Test

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
