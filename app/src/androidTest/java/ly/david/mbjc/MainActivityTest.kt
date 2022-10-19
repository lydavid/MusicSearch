package ly.david.mbjc

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import ly.david.mbjc.ui.MainActivity
import org.junit.Rule

internal abstract class MainActivityTest {
    @get:Rule(order = 0)
    val hiltRule: HiltAndroidRule by lazy { HiltAndroidRule(this) }

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
}
