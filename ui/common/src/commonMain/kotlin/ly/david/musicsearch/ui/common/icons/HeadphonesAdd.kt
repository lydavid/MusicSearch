package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Stitched from Headphones and AddLink.
val CustomIcons.HeadphonesAdd: ImageVector
    get() {
        if (_HeadphonesAdd != null) {
            return _HeadphonesAdd!!
        }
        _HeadphonesAdd = ImageVector.Builder(
            name = "HeadphonesAdd",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                // Plus icon in bottom-right (matches AddLink style)
                moveTo(680f, 880f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(-120f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(80f)
                horizontalLineToRelative(-120f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(-80f)
                close()

                // Headphones with right ear cup removed to carve space for the plus.
                // The headband arc + left ear cup are identical to the original.
                // The right side only draws the stub that connects the arc down to y=520,
                // then stops — leaving the bottom-right quadrant clear for the plus.
                moveTo(360f, 840f)
                lineTo(200f, 840f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 760f)
                verticalLineToRelative(-280f)
                quadToRelative(0f, -75f, 28.5f, -140.5f)
                reflectiveQuadToRelative(77f, -114f)
                quadToRelative(48.5f, -48.5f, 114f, -77f)
                reflectiveQuadTo(480f, 120f)
                quadToRelative(75f, 0f, 140.5f, 28.5f)
                reflectiveQuadToRelative(114f, 77f)
                quadToRelative(48.5f, 48.5f, 77f, 114f)
                reflectiveQuadTo(840f, 480f)
                verticalLineToRelative(40f)

                // Right stub: narrow band entry, stops at y=520, no full ear cup
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-40f)
                quadToRelative(0f, -117f, -81.5f, -198.5f)
                reflectiveQuadTo(480f, 200f)
                quadToRelative(-117f, 0f, -198.5f, 81.5f)
                reflectiveQuadTo(200f, 480f)
                verticalLineToRelative(40f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(320f)
                close()

                // Left ear cup inner pad
                moveTo(280f, 600f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(160f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(-160f)
                close()
            }
        }.build()

        return _HeadphonesAdd!!
    }

@Suppress("ObjectPropertyName")
private var _HeadphonesAdd: ImageVector? = null
