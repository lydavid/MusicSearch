package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.CorporateFare: ImageVector
    get() {
        if (_CorporateFare != null) {
            return _CorporateFare!!
        }
        _CorporateFare = ImageVector.Builder(
            name = "CorporateFare",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(80f, 840f)
                verticalLineToRelative(-720f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(160f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(560f)
                lineTo(80f, 840f)
                close()
                moveTo(160f, 760f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(-80f)
                lineTo(160f, 680f)
                verticalLineToRelative(80f)
                close()
                moveTo(160f, 600f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(-80f)
                lineTo(160f, 520f)
                verticalLineToRelative(80f)
                close()
                moveTo(160f, 440f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(-80f)
                lineTo(160f, 360f)
                verticalLineToRelative(80f)
                close()
                moveTo(160f, 280f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(-80f)
                lineTo(160f, 200f)
                verticalLineToRelative(80f)
                close()
                moveTo(480f, 760f)
                horizontalLineToRelative(320f)
                verticalLineToRelative(-400f)
                lineTo(480f, 360f)
                verticalLineToRelative(400f)
                close()
                moveTo(560f, 520f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(80f)
                lineTo(560f, 520f)
                close()
                moveTo(560f, 680f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(80f)
                lineTo(560f, 680f)
                close()
            }
        }.build()

        return _CorporateFare!!
    }

@Suppress("ObjectPropertyName")
private var _CorporateFare: ImageVector? = null
