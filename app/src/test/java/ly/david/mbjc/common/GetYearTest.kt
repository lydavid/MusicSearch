package ly.david.mbjc.common

import ly.david.mbjc.ui.common.getYear
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetYearTest {

    @Test
    fun `get year success`() {
        assertEquals("2021", "2021-09-08".getYear())
    }

    @Test
    fun `get year empty string`() {
        assertEquals("", "".getYear())
    }

    @Test
    fun `get year too short`() {
        assertEquals("", "202".getYear())
    }
}
