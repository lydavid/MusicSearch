package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.ArrowDropUp: ImageVector
    get() {
        if (_ArrowDropUp != null) {
            return _ArrowDropUp!!
        }
        _ArrowDropUp = ImageVector.Builder(
            name = "ArrowDropUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(7f, 14f)
                lineToRelative(5f, -5f)
                lineToRelative(5f, 5f)
                close()
            }
        }.build()

        return _ArrowDropUp!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDropUp: ImageVector? = null
