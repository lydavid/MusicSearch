package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.common.getYear
import ly.david.musicsearch.shared.domain.common.prependHttps
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.common.toUUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

internal class StringExtTest {

    // region getYear
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
    // endregion

    // region toFlagEmoji
    @Test
    fun `valid country code`() {
        assertEquals("\uD83C\uDDE8\uD83C\uDDE6", "CA".toFlagEmoji())
    }

    @Test
    fun `global code`() {
        assertEquals("\uD83C\uDF10", "XW".toFlagEmoji())
    }

    @Test
    fun `European Union code`() {
        assertEquals("\uD83C\uDDEA\uD83C\uDDFA", "XE".toFlagEmoji())
    }

    @Test
    fun `empty string`() {
        assertEquals("", "".toFlagEmoji())
    }

    @Test
    fun `invalid country code with 3 letters`() {
        assertEquals("CAN", "CAN".toFlagEmoji())
    }

    @Test
    fun `invalid country code with 1 letter`() {
        assertEquals("C", "C".toFlagEmoji())
    }

    @Test
    fun `invalid country code with first character is not letter`() {
        assertEquals("#C", "#C".toFlagEmoji())
    }

    @Test
    fun `invalid country code with second character is not letter`() {
        assertEquals("C3", "C3".toFlagEmoji())
    }
    // endregion

    // region useHttps
    @Test
    fun `prepend https`() {
        assertEquals(
            "https://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185",
            "coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185".prependHttps(),
        )
    }
    // endregion

    // region toUUID
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `valid UUIDs`() {
        assertEquals(
            "f81cbdf9-4390-4738-b6b2-124f5bceafe3",
            "f81cbdf9-4390-4738-b6b2-124f5bceafe3".toUUID().toHexDashString(),
        )
        assertEquals(
            "f81cbdf9-4390-4738-b6b2-124f5bceafe3",
            "F81CBDF9-4390-4738-B6B2-124F5BCEAFE3".toUUID().toHexDashString(),
        )
        assertEquals(
            "862e2705-220f-4029-bed5-b46c2ed0af24",
            "https://musicbrainz.org/recording/862e2705-220f-4029-bed5-b46c2ed0af24".toUUID().toHexDashString(),
        )
        assertEquals(
            "862e2705-220f-4029-bed5-b46c2ed0af24",
            "https://musicbrainz.org/recording/862e2705-220f-4029-bed5-b46c2ed0af24/".toUUID().toHexDashString(),
        )
        assertEquals(
            "993169b9-ed8e-49aa-8f6a-81e8bbcf84ba",
            "listenbrainz.org/track/993169b9-ed8e-49aa-8f6a-81e8bbcf84ba".toUUID().toHexDashString(),
        )
        assertEquals(
            "f81cbdf9-4390-4738-b6b2-124f5bceafe3",
            "https://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185".toUUID()
                .toHexDashString(),
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `invalid UUIDs`() {
        assertFailsWith<IllegalArgumentException> { "".toUUID() }
        assertFailsWith<IllegalArgumentException> { " ".toUUID() }
        assertFailsWith<IllegalArgumentException> { "\n".toUUID() }
        assertFailsWith<IllegalArgumentException> { "f81cbdf9-4390-4738-b6b2-124f5bceafe".toUUID() }
        assertFailsWith<IllegalArgumentException> { "f81cbdf9-4390-4738-b6b2-124f5bceafex".toUUID() }
    }
    // endregion
}
