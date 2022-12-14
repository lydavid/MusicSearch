package ly.david.mbjc

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ly.david.mbjc.ui.MainActivity

internal interface StringReferences {
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

    val searchDrawerLabel
        get() = composeTestRule.activity.resources.getString(R.string.search_musicbrainz)
    val searchLabel
        get() = composeTestRule.activity.resources.getString(R.string.search)
    val clearSearchContentDescription
        get() = composeTestRule.activity.resources.getString(R.string.clear_search)
    val emptySearchWarning
        get() = composeTestRule.activity.resources.getString(R.string.search_cannot_be_empty)
    val appName
        get() = composeTestRule.activity.resources.getString(R.string.app_name)
    val navDrawerIconContentDescription
        get() = composeTestRule.activity.resources.getString(R.string.open_nav_drawer)
    val historyDrawerLabel
        get() = composeTestRule.activity.resources.getString(R.string.history)
    val historyScreenTitle
        get() = composeTestRule.activity.resources.getString(R.string.recent_history)
    val noResultsFound
        get() = composeTestRule.activity.resources.getString(R.string.no_results_found)
    val places
        get() = composeTestRule.activity.resources.getString(R.string.places)
    val releases
        get() = composeTestRule.activity.resources.getString(R.string.releases)
    val stats
        get() = composeTestRule.activity.resources.getString(R.string.stats)
    val tracks
        get() = composeTestRule.activity.resources.getString(R.string.tracks)
    val filter
        get() = composeTestRule.activity.resources.getString(R.string.filter)
    val relationships
        get() = composeTestRule.activity.resources.getString(R.string.relationships)
    val resourceLabel
        get() = composeTestRule.activity.resources.getString(R.string.resource)
    val ok
        get() = composeTestRule.activity.resources.getString(R.string.ok)
    val back
        get() = composeTestRule.activity.resources.getString(R.string.back)
    val details
        get() = composeTestRule.activity.resources.getString(R.string.details)
}
