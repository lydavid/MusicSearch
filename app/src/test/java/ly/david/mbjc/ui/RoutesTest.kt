package ly.david.mbjc.ui

import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.navigation.getTopLevelDestination
import ly.david.mbjc.ui.navigation.getTopLevelRoute
import org.junit.Assert.assertEquals
import org.junit.Test

class RoutesTest {

    @Test
    fun `get top-level route of top-level route`() {
        assertEquals(Destination.LOOKUP.route, Destination.LOOKUP.route.getTopLevelRoute())
    }

    @Test
    fun `get top-level destination of top-level destination`() {
        assertEquals(Destination.HISTORY, Destination.HISTORY.route.getTopLevelRoute().getTopLevelDestination())
    }

    // TODO: we just have LOOKUP as a fallback case, until we have other sub-routes, we can't test this
//    @Test
//    fun `get top-level destination of non-top-level destination without stripping`() {
//        assertEquals(Destination.LOOKUP, Destination.LOOKUP_ARTIST.route.getTopLevelDestination())
//    }

    @Test
    fun `get top-level route of non-top-level route`() {
        assertEquals(Destination.LOOKUP.route, Destination.LOOKUP_ARTIST.route.getTopLevelRoute())
    }

    @Test
    fun `get top-level destination of non-top-level destination`() {
        assertEquals(Destination.LOOKUP, Destination.LOOKUP_ARTIST.route.getTopLevelRoute().getTopLevelDestination())
    }

    @Test
    fun `get top-level route of non-top-level route with parameter`() {
        assertEquals(Destination.LOOKUP.route, "${Destination.LOOKUP_RELEASE_GROUP.route}/123".getTopLevelRoute())
    }

    @Test
    fun `get top-level destination of non-top-level route with destination`() {
        assertEquals(Destination.LOOKUP, "${Destination.LOOKUP_RELEASE_GROUP.route}/123".getTopLevelRoute().getTopLevelDestination())
    }
}
