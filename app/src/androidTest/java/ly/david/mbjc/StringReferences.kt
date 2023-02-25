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
    val history
        get() = composeTestRule.activity.resources.getString(R.string.history)
    val collections
        get() = composeTestRule.activity.resources.getString(R.string.collections)
    val settings
        get() = composeTestRule.activity.resources.getString(R.string.settings)
    val historyScreenTitle
        get() = composeTestRule.activity.resources.getString(R.string.recent_history)
    val noResultsFound
        get() = composeTestRule.activity.resources.getString(R.string.no_results_found)
    val events
        get() = composeTestRule.activity.resources.getString(R.string.events)
    val places
        get() = composeTestRule.activity.resources.getString(R.string.places)
    val recordings
        get() = composeTestRule.activity.resources.getString(R.string.recordings)
    val releases
        get() = composeTestRule.activity.resources.getString(R.string.releases)
    val releaseGroups
        get() = composeTestRule.activity.resources.getString(R.string.release_groups)
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
    val retry
        get() = composeTestRule.activity.resources.getString(R.string.retry)
    val cancel
        get() = composeTestRule.activity.resources.getString(R.string.cancel)
    val theme
        get() = composeTestRule.activity.resources.getString(R.string.theme)
    val light
        get() = composeTestRule.activity.resources.getString(R.string.light)
    val dark
        get() = composeTestRule.activity.resources.getString(R.string.dark)
    val system
        get() = composeTestRule.activity.resources.getString(R.string.system)
}
