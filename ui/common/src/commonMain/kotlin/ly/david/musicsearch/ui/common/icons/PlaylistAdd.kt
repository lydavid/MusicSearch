package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.PlaylistAdd: ImageVector
    get() {
        if (_PlaylistAdd != null) {
            return _PlaylistAdd!!
        }
        _PlaylistAdd = ImageVector.Builder(
            name = "PlaylistAdd",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(120f, 640f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(280f)
                verticalLineToRelative(80f)
                lineTo(120f, 640f)
                close()
                moveTo(120f, 480f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(440f)
                verticalLineToRelative(80f)
                lineTo(120f, 480f)
                close()
                moveTo(120f, 320f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(440f)
                verticalLineToRelative(80f)
                lineTo(120f, 320f)
                close()
                moveTo(640f, 800f)
                verticalLineToRelative(-160f)
                lineTo(480f, 640f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(-160f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(160f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(80f)
                lineTo(720f, 640f)
                verticalLineToRelative(160f)
                horizontalLineToRelative(-80f)
                close()
            }
        }.build()

        return _PlaylistAdd!!
    }

@Suppress("ObjectPropertyName")
private var _PlaylistAdd: ImageVector? = null
