package ly.david.data

import org.junit.Assert
import org.junit.Test

internal class CoordinatesTest {

    private lateinit var coordinates: CoordinatesUiModel

    @Test
    fun `null lat, null long`() {
        coordinates = CoordinatesUiModel(
            latitude = null,
            longitude = null
        )
        Assert.assertNull(coordinates.formatForDisplay())
    }

    @Test
    fun `null lat, has long`() {
        coordinates = CoordinatesUiModel(
            latitude = null,
            longitude = 0.0
        )
        Assert.assertNull("", coordinates.formatForDisplay())
    }

    @Test
    fun `has lat, null long`() {
        coordinates = CoordinatesUiModel(
            latitude = 0.0,
            longitude = null
        )
        Assert.assertNull("", coordinates.formatForDisplay())
    }

    @Test
    fun `N lat, E long`() {
        coordinates = CoordinatesUiModel(
            latitude = 0.0,
            longitude = 0.0
        )
        Assert.assertEquals("0.0°N, 0.0°E", coordinates.formatForDisplay())
    }

    @Test
    fun `N lat, W long`() {
        coordinates = CoordinatesUiModel(
            latitude = 0.0,
            longitude = -1.0
        )
        Assert.assertEquals("0.0°N, 1.0°W", coordinates.formatForDisplay())
    }

    @Test
    fun `S lat, E long`() {
        coordinates = CoordinatesUiModel(
            latitude = -1.0,
            longitude = 0.0
        )
        Assert.assertEquals("1.0°S, 0.0°E", coordinates.formatForDisplay())
    }

    @Test
    fun `S lat, W long`() {
        coordinates = CoordinatesUiModel(
            latitude = -1.0,
            longitude = -1.0
        )
        Assert.assertEquals("1.0°S, 1.0°W", coordinates.formatForDisplay())
    }

    @Test
    fun `more decimals`() {
        coordinates = CoordinatesUiModel(
            latitude = 1.23456789,
            longitude = 73.98905
        )
        Assert.assertEquals("1.23456789°N, 73.98905°E", coordinates.formatForDisplay())
    }
}
