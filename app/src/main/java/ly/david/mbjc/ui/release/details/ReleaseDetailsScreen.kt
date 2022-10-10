package ly.david.mbjc.ui.release.details

import androidx.compose.runtime.Composable

@Composable
internal fun ReleaseDetailsScreen(

) {

    // TODO: page release events
    //  release_events not releases_events
    //  - release_id, date, area_id (though not FK?)
    //  Clicking on card goes to Area, where we will actually fetch/insert it
    //  Deleting release will delete release_event
    //  Deleting area will not delete release_event

    // TODO: should we make a single release lookup call when landing in release screen?
    //  otherwise this screen will make a 2nd call just for labels
    //  release events are always included already
}
