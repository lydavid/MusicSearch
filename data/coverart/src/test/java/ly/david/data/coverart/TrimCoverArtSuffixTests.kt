package ly.david.data.coverart

import org.junit.Assert.assertEquals
import org.junit.Test

class TrimCoverArtSuffixTests {

    @Test
    fun thumbnail() {
        assertEquals(
            "https://coverartarchive.org/release/e6a9a248-649c-4be1-bc84-924638bafa49/32187188956",
            "https://coverartarchive.org/release/e6a9a248-649c-4be1-bc84-924638bafa49/32187188956-250".trimCoverArtSuffix()
        )
    }

    @Test
    fun `non-thumbnail`() {
        assertEquals(
            "https://coverartarchive.org/release/e6a9a248-649c-4be1-bc84-924638bafa49/32187188956",
            "https://coverartarchive.org/release/e6a9a248-649c-4be1-bc84-924638bafa49/32187188956-500".trimCoverArtSuffix()
        )
    }
}
