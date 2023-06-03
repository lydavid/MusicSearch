package ly.david.mbjc.ui.place

import ly.david.ui.common.topappbar.Tab

internal enum class PlaceTab(val tab: Tab) {
    DETAILS(Tab.DETAILS),

    // TODO: Should exclude event-rels because they appear to be the same as the results from browse events by place
    RELATIONSHIPS(Tab.RELATIONSHIPS),
    EVENTS(Tab.EVENTS),
    STATS(Tab.STATS)
}
