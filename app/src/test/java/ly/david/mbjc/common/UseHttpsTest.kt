package ly.david.mbjc.common

import ly.david.mbjc.ui.common.useHttps
import org.junit.Assert.assertEquals
import org.junit.Test

class UseHttpsTest {

    @Test
    fun `convert http to https`() {
        assertEquals(
            "https://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185.jpg",
            "http://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185.jpg".useHttps()
        )
    }
}
