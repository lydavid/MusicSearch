package ly.david.mbjc

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ly.david.mbjc.ui.MainActivity

internal interface StringReferences {
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

    private val activity
        get() = composeTestRule.activity

    val deeplinkSchema
        get() = activity.getString(R.string.deeplink_schema)
    val searchDrawerLabel
        get() = activity.getString(R.string.search_musicbrainz)
    val searchLabel
        get() = activity.getString(R.string.search)
    val clearSearchContentDescription
        get() = activity.getString(R.string.clear_search)
    val emptySearchWarning
        get() = activity.getString(R.string.search_cannot_be_empty)
    val appName
        get() = activity.getString(R.string.app_name)
    val history
        get() = activity.getString(R.string.history)
    val collections
        get() = activity.getString(R.string.collections)
    val settings
        get() = activity.getString(R.string.settings)
    val historyScreenTitle
        get() = activity.getString(R.string.recent_history)
    val noResultsFound
        get() = activity.getString(R.string.no_results_found)
    val events
        get() = activity.getString(R.string.events)
    val places
        get() = activity.getString(R.string.places)
    val recordings
        get() = activity.getString(R.string.recordings)
    val releases
        get() = activity.getString(R.string.releases)
    val releaseGroups
        get() = activity.getString(R.string.release_groups)
    val stats
        get() = activity.getString(R.string.stats)
    val tracks
        get() = activity.getString(R.string.tracks)
    val filter
        get() = activity.getString(R.string.filter)
    val relationships
        get() = activity.getString(R.string.relationships)
    val resourceLabel
        get() = activity.getString(R.string.resource)
    val ok
        get() = activity.getString(R.string.ok)
    val back
        get() = activity.getString(R.string.back)
    val details
        get() = activity.getString(R.string.details)
    val retry
        get() = activity.getString(R.string.retry)
    val cancel
        get() = activity.getString(R.string.cancel)
    val theme
        get() = activity.getString(R.string.theme)
    val light
        get() = activity.getString(R.string.light)
    val dark
        get() = activity.getString(R.string.dark)
    val system
        get() = activity.getString(R.string.system)
    val moreActions
        get() = activity.getString(R.string.more_actions)
    val sort
        get() = activity.getString(R.string.sort)
    val unsort
        get() = activity.getString(R.string.unsort)
    val showMoreInfo
        get() = activity.getString(R.string.show_more_info)
    val showLessInfo
        get() = activity.getString(R.string.show_less_info)
}
