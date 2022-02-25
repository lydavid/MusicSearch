package ly.david.musicbrainzjetpackcompose.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class RoutesTest {

    @Test
    fun `get top-level route of top-level route`() {
        assertEquals(Routes.SEARCH, Routes.getTopLevelRoute(Routes.SEARCH))
    }

    @Test
    fun `get top-level route of non-top-level route`() {
        assertEquals(Routes.SEARCH, Routes.getTopLevelRoute(Routes.LOOKUP_ARTIST))
    }
}
