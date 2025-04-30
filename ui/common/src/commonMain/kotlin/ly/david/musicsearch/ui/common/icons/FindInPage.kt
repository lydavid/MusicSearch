package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.FindInPage: ImageVector
    get() {
        if (_FindInPage != null) {
            return _FindInPage!!
        }
        _FindInPage = ImageVector.Builder(
            name = "FindInPage",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(14f, 2f)
                lineTo(6f, 2f)
                curveToRelative(-1.1f, 0f, -1.99f, 0.9f, -1.99f, 2f)
                lineTo(4f, 20f)
                curveToRelative(0f, 1.1f, 0.89f, 2f, 1.99f, 2f)
                lineTo(18f, 22f)
                curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
                lineTo(20f, 8f)
                lineToRelative(-6f, -6f)
                close()
                moveTo(6f, 4f)
                horizontalLineToRelative(7f)
                lineToRelative(5f, 5f)
                verticalLineToRelative(8.58f)
                lineToRelative(-1.84f, -1.84f)
                curveToRelative(1.28f, -1.94f, 1.07f, -4.57f, -0.64f, -6.28f)
                curveTo(14.55f, 8.49f, 13.28f, 8f, 12f, 8f)
                curveToRelative(-1.28f, 0f, -2.55f, 0.49f, -3.53f, 1.46f)
                curveToRelative(-1.95f, 1.95f, -1.95f, 5.11f, 0f, 7.05f)
                curveToRelative(0.97f, 0.97f, 2.25f, 1.46f, 3.53f, 1.46f)
                curveToRelative(0.96f, 0f, 1.92f, -0.28f, 2.75f, -0.83f)
                lineTo(17.6f, 20f)
                lineTo(6f, 20f)
                lineTo(6f, 4f)
                close()
                moveTo(14.11f, 15.1f)
                curveToRelative(-0.56f, 0.56f, -1.31f, 0.88f, -2.11f, 0.88f)
                reflectiveCurveToRelative(-1.55f, -0.31f, -2.11f, -0.88f)
                curveToRelative(-0.56f, -0.56f, -0.88f, -1.31f, -0.88f, -2.11f)
                reflectiveCurveToRelative(0.31f, -1.55f, 0.88f, -2.11f)
                curveToRelative(0.56f, -0.57f, 1.31f, -0.88f, 2.11f, -0.88f)
                reflectiveCurveToRelative(1.55f, 0.31f, 2.11f, 0.88f)
                curveToRelative(0.56f, 0.56f, 0.88f, 1.31f, 0.88f, 2.11f)
                reflectiveCurveToRelative(-0.31f, 1.55f, -0.88f, 2.11f)
                close()
            }
        }.build()

        return _FindInPage!!
    }

@Suppress("ObjectPropertyName")
private var _FindInPage: ImageVector? = null
