package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.TheaterComedy: ImageVector
    get() {
        if (_TheaterComedy != null) {
            return _TheaterComedy!!
        }
        _TheaterComedy = ImageVector.Builder(
            name = "TheaterComedy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(760f, 300f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(800f, 260f)
                quadToRelative(0f, -17f, -11.5f, -28.5f)
                reflectiveQuadTo(760f, 220f)
                quadToRelative(-17f, 0f, -28.5f, 11.5f)
                reflectiveQuadTo(720f, 260f)
                quadToRelative(0f, 17f, 11.5f, 28.5f)
                reflectiveQuadTo(760f, 300f)
                close()
                moveTo(600f, 300f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(640f, 260f)
                quadToRelative(0f, -17f, -11.5f, -28.5f)
                reflectiveQuadTo(600f, 220f)
                quadToRelative(-17f, 0f, -28.5f, 11.5f)
                reflectiveQuadTo(560f, 260f)
                quadToRelative(0f, 17f, 11.5f, 28.5f)
                reflectiveQuadTo(600f, 300f)
                close()
                moveTo(580f, 436f)
                horizontalLineToRelative(200f)
                quadToRelative(0f, -35f, -30.5f, -55.5f)
                reflectiveQuadTo(680f, 360f)
                quadToRelative(-39f, 0f, -69.5f, 20.5f)
                reflectiveQuadTo(580f, 436f)
                close()
                moveTo(280f, 880f)
                quadToRelative(-100f, 0f, -170f, -70f)
                reflectiveQuadTo(40f, 640f)
                verticalLineToRelative(-280f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(280f)
                quadToRelative(0f, 100f, -70f, 170f)
                reflectiveQuadTo(280f, 880f)
                close()
                moveTo(280f, 800f)
                quadToRelative(66f, 0f, 113f, -47f)
                reflectiveQuadToRelative(47f, -113f)
                verticalLineToRelative(-200f)
                lineTo(120f, 440f)
                verticalLineToRelative(200f)
                quadToRelative(0f, 66f, 47f, 113f)
                reflectiveQuadToRelative(113f, 47f)
                close()
                moveTo(680f, 600f)
                quadToRelative(-26f, 0f, -51.5f, -5.5f)
                reflectiveQuadTo(580f, 578f)
                verticalLineToRelative(-94f)
                quadToRelative(22f, 17f, 47.5f, 26.5f)
                reflectiveQuadTo(680f, 520f)
                quadToRelative(66f, 0f, 113f, -47f)
                reflectiveQuadToRelative(47f, -113f)
                verticalLineToRelative(-200f)
                lineTo(520f, 160f)
                verticalLineToRelative(140f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-220f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(280f)
                quadToRelative(0f, 100f, -70f, 170f)
                reflectiveQuadToRelative(-170f, 70f)
                close()
                moveTo(200f, 580f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(240f, 540f)
                quadToRelative(0f, -17f, -11.5f, -28.5f)
                reflectiveQuadTo(200f, 500f)
                quadToRelative(-17f, 0f, -28.5f, 11.5f)
                reflectiveQuadTo(160f, 540f)
                quadToRelative(0f, 17f, 11.5f, 28.5f)
                reflectiveQuadTo(200f, 580f)
                close()
                moveTo(360f, 580f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(400f, 540f)
                quadToRelative(0f, -17f, -11.5f, -28.5f)
                reflectiveQuadTo(360f, 500f)
                quadToRelative(-17f, 0f, -28.5f, 11.5f)
                reflectiveQuadTo(320f, 540f)
                quadToRelative(0f, 17f, 11.5f, 28.5f)
                reflectiveQuadTo(360f, 580f)
                close()
                moveTo(280f, 716f)
                quadToRelative(39f, 0f, 69.5f, -20.5f)
                reflectiveQuadTo(380f, 640f)
                lineTo(180f, 640f)
                quadToRelative(0f, 35f, 30.5f, 55.5f)
                reflectiveQuadTo(280f, 716f)
                close()
                moveTo(280f, 620f)
                close()
                moveTo(680f, 340f)
                close()
            }
        }.build()

        return _TheaterComedy!!
    }

@Suppress("ObjectPropertyName")
private var _TheaterComedy: ImageVector? = null
