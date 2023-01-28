package ly.david.data.coverart

import org.junit.Assert.assertEquals
import org.junit.Test

class BuildCoverArtUrlTests {

    @Test
    fun empty() {
        assertEquals(
            "",
            buildCoverArtUrl("")
        )
    }

    @Test
    fun thumbnail() {
        assertEquals(
            "https://coverartarchive.org/release/e6a9a248-649c-4be1-bc84-924638bafa49/32187188956-250",
            buildCoverArtUrl("e6a9a248-649c-4be1-bc84-924638bafa49/32187188956")
        )
    }

    @Test
    fun `non-thumbnail`() {
        assertEquals(
            "https://coverartarchive.org/release/e6a9a248-649c-4be1-bc84-924638bafa49/32187188956-500",
            buildCoverArtUrl("e6a9a248-649c-4be1-bc84-924638bafa49/32187188956", thumbnail = false)
        )
    }
}
