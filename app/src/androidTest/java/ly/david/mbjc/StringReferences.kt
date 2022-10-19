package ly.david.mbjc

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ly.david.mbjc.ui.MainActivity

internal interface StringReferences {
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

    fun getSearchDrawerLabel() = composeTestRule.activity.resources.getString(R.string.search_musicbrainz)
    fun getSearchLabel() = composeTestRule.activity.resources.getString(R.string.search)
    fun getClearSearchContentDescription() = composeTestRule.activity.resources.getString(R.string.clear_search)
    fun getAppName() = composeTestRule.activity.resources.getString(R.string.app_name)
    fun getNavDrawerIconContentDescription() = composeTestRule.activity.resources.getString(R.string.open_nav_drawer)
    fun getHistoryDrawerLabel() = composeTestRule.activity.resources.getString(R.string.history)
    fun getHistoryScreenTitle() = composeTestRule.activity.resources.getString(R.string.recent_history)
}
