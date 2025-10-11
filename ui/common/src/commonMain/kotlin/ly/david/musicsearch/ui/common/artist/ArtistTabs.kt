package ly.david.musicsearch.ui.common.artist

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.ui.common.topappbar.Tab

val artistTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELEASE_GROUPS,
    Tab.RELEASES,
    Tab.RECORDINGS,
    Tab.WORKS,
    Tab.EVENTS,
    Tab.RELATIONSHIPS,
)
