package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.ImportContacts: ImageVector
    get() {
        if (_ImportContacts != null) {
            return _ImportContacts!!
        }
        _ImportContacts = ImageVector.Builder(
            name = "ImportContacts",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(260f, 640f)
                quadToRelative(47f, 0f, 91.5f, 10.5f)
                reflectiveQuadTo(440f, 682f)
                verticalLineToRelative(-394f)
                quadToRelative(-41f, -24f, -87f, -36f)
                reflectiveQuadToRelative(-93f, -12f)
                quadToRelative(-36f, 0f, -71.5f, 7f)
                reflectiveQuadTo(120f, 268f)
                verticalLineToRelative(396f)
                quadToRelative(35f, -12f, 69.5f, -18f)
                reflectiveQuadToRelative(70.5f, -6f)
                close()
                moveTo(520f, 682f)
                quadToRelative(44f, -21f, 88.5f, -31.5f)
                reflectiveQuadTo(700f, 640f)
                quadToRelative(36f, 0f, 70.5f, 6f)
                reflectiveQuadToRelative(69.5f, 18f)
                verticalLineToRelative(-396f)
                quadToRelative(-33f, -14f, -68.5f, -21f)
                reflectiveQuadToRelative(-71.5f, -7f)
                quadToRelative(-47f, 0f, -93f, 12f)
                reflectiveQuadToRelative(-87f, 36f)
                verticalLineToRelative(394f)
                close()
                moveTo(480f, 800f)
                quadToRelative(-48f, -38f, -104f, -59f)
                reflectiveQuadToRelative(-116f, -21f)
                quadToRelative(-42f, 0f, -82.5f, 11f)
                reflectiveQuadTo(100f, 762f)
                quadToRelative(-21f, 11f, -40.5f, -1f)
                reflectiveQuadTo(40f, 726f)
                verticalLineToRelative(-482f)
                quadToRelative(0f, -11f, 5.5f, -21f)
                reflectiveQuadTo(62f, 208f)
                quadToRelative(46f, -24f, 96f, -36f)
                reflectiveQuadToRelative(102f, -12f)
                quadToRelative(58f, 0f, 113.5f, 15f)
                reflectiveQuadTo(480f, 220f)
                quadToRelative(51f, -30f, 106.5f, -45f)
                reflectiveQuadTo(700f, 160f)
                quadToRelative(52f, 0f, 102f, 12f)
                reflectiveQuadToRelative(96f, 36f)
                quadToRelative(11f, 5f, 16.5f, 15f)
                reflectiveQuadToRelative(5.5f, 21f)
                verticalLineToRelative(482f)
                quadToRelative(0f, 23f, -19.5f, 35f)
                reflectiveQuadToRelative(-40.5f, 1f)
                quadToRelative(-37f, -20f, -77.5f, -31f)
                reflectiveQuadTo(700f, 720f)
                quadToRelative(-60f, 0f, -116f, 21f)
                reflectiveQuadToRelative(-104f, 59f)
                close()
                moveTo(280f, 466f)
                close()
            }
        }.build()

        return _ImportContacts!!
    }

@Suppress("ObjectPropertyName")
private var _ImportContacts: ImageVector? = null
