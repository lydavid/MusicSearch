package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// https://commons.wikimedia.org/wiki/File:Wikipedia%27s_W.svg
val CustomIcons.Wikipedia: ImageVector
    get() {
        if (_Wikipedia != null) {
            return _Wikipedia!!
        }
        _Wikipedia = ImageVector.Builder(
            name = "Wikipedia",
            defaultWidth = 128.dp,
            defaultHeight = 128.dp,
            viewportWidth = 128f,
            viewportHeight = 128f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(120.85f, 29.21f)
                curveTo(120.85f, 29.62f, 120.72f, 29.99f, 120.47f, 30.33f)
                curveTo(120.21f, 30.66f, 119.94f, 30.83f, 119.63f, 30.83f)
                curveTo(117.14f, 31.07f, 115.09f, 31.87f, 113.51f, 33.24f)
                curveTo(111.92f, 34.6f, 110.29f, 37.21f, 108.6f, 41.05f)
                lineTo(82.8f, 99.19f)
                curveTo(82.63f, 99.73f, 82.16f, 100f, 81.38f, 100f)
                curveTo(80.77f, 100f, 80.3f, 99.73f, 79.96f, 99.19f)
                lineTo(65.49f, 68.93f)
                lineTo(48.85f, 99.19f)
                curveTo(48.51f, 99.73f, 48.04f, 100f, 47.43f, 100f)
                curveTo(46.69f, 100f, 46.2f, 99.73f, 45.96f, 99.19f)
                lineTo(20.61f, 41.05f)
                curveTo(19.03f, 37.44f, 17.36f, 34.92f, 15.6f, 33.49f)
                curveTo(13.85f, 32.06f, 11.4f, 31.17f, 8.27f, 30.83f)
                curveTo(8f, 30.83f, 7.74f, 30.69f, 7.51f, 30.4f)
                curveTo(7.27f, 30.12f, 7.15f, 29.79f, 7.15f, 29.42f)
                curveTo(7.15f, 28.47f, 7.42f, 28f, 7.96f, 28f)
                curveTo(10.22f, 28f, 12.58f, 28.1f, 15.05f, 28.3f)
                curveTo(17.34f, 28.51f, 19.5f, 28.61f, 21.52f, 28.61f)
                curveTo(23.58f, 28.61f, 26.01f, 28.51f, 28.81f, 28.3f)
                curveTo(31.74f, 28.1f, 34.34f, 28f, 36.6f, 28f)
                curveTo(37.14f, 28f, 37.41f, 28.47f, 37.41f, 29.42f)
                curveTo(37.41f, 30.36f, 37.24f, 30.83f, 36.91f, 30.83f)
                curveTo(34.65f, 31f, 32.87f, 31.58f, 31.57f, 32.55f)
                curveTo(30.27f, 33.53f, 29.62f, 34.81f, 29.62f, 36.4f)
                curveTo(29.62f, 37.21f, 29.89f, 38.22f, 30.43f, 39.43f)
                lineTo(51.38f, 86.74f)
                lineTo(63.27f, 64.28f)
                lineTo(52.19f, 41.05f)
                curveTo(50.2f, 36.91f, 48.56f, 34.23f, 47.28f, 33.03f)
                curveTo(46f, 31.84f, 44.06f, 31.1f, 41.46f, 30.83f)
                curveTo(41.22f, 30.83f, 41f, 30.69f, 40.78f, 30.4f)
                curveTo(40.56f, 30.12f, 40.45f, 29.79f, 40.45f, 29.42f)
                curveTo(40.45f, 28.47f, 40.68f, 28f, 41.16f, 28f)
                curveTo(43.42f, 28f, 45.49f, 28.1f, 47.38f, 28.3f)
                curveTo(49.2f, 28.51f, 51.14f, 28.61f, 53.2f, 28.61f)
                curveTo(55.22f, 28.61f, 57.36f, 28.51f, 59.62f, 28.3f)
                curveTo(61.95f, 28.1f, 64.24f, 28f, 66.5f, 28f)
                curveTo(67.04f, 28f, 67.31f, 28.47f, 67.31f, 29.42f)
                curveTo(67.31f, 30.36f, 67.15f, 30.83f, 66.81f, 30.83f)
                curveTo(62.29f, 31.14f, 60.03f, 32.42f, 60.03f, 34.68f)
                curveTo(60.03f, 35.69f, 60.55f, 37.26f, 61.6f, 39.38f)
                lineTo(68.93f, 54.26f)
                lineTo(76.22f, 40.65f)
                curveTo(77.23f, 38.73f, 77.74f, 37.11f, 77.74f, 35.79f)
                curveTo(77.74f, 32.69f, 75.48f, 31.04f, 70.96f, 30.83f)
                curveTo(70.55f, 30.83f, 70.35f, 30.36f, 70.35f, 29.42f)
                curveTo(70.35f, 29.08f, 70.45f, 28.76f, 70.65f, 28.46f)
                curveTo(70.86f, 28.15f, 71.06f, 28f, 71.26f, 28f)
                curveTo(72.88f, 28f, 74.87f, 28.1f, 77.23f, 28.3f)
                curveTo(79.49f, 28.51f, 81.35f, 28.61f, 82.8f, 28.61f)
                curveTo(83.84f, 28.61f, 85.38f, 28.52f, 87.4f, 28.35f)
                curveTo(89.96f, 28.12f, 92.11f, 28f, 93.83f, 28f)
                curveTo(94.23f, 28f, 94.43f, 28.4f, 94.43f, 29.21f)
                curveTo(94.43f, 30.29f, 94.06f, 30.83f, 93.32f, 30.83f)
                curveTo(90.69f, 31.1f, 88.57f, 31.83f, 86.97f, 33.01f)
                curveTo(85.37f, 34.19f, 83.37f, 36.87f, 80.98f, 41.05f)
                lineTo(71.26f, 59.02f)
                lineTo(84.42f, 85.83f)
                lineTo(103.85f, 40.65f)
                curveTo(104.52f, 39f, 104.86f, 37.48f, 104.86f, 36.1f)
                curveTo(104.86f, 32.79f, 102.6f, 31.04f, 98.08f, 30.83f)
                curveTo(97.67f, 30.83f, 97.47f, 30.36f, 97.47f, 29.42f)
                curveTo(97.47f, 28.47f, 97.77f, 28f, 98.38f, 28f)
                curveTo(100.03f, 28f, 101.99f, 28.1f, 104.25f, 28.3f)
                curveTo(106.34f, 28.51f, 108.1f, 28.61f, 109.51f, 28.61f)
                curveTo(111f, 28.61f, 112.72f, 28.51f, 114.67f, 28.3f)
                curveTo(116.7f, 28.1f, 118.52f, 28f, 120.14f, 28f)
                curveTo(120.61f, 28f, 120.85f, 28.4f, 120.85f, 29.21f)
                close()
            }
        }.build()

        return _Wikipedia!!
    }

@Suppress("ObjectPropertyName")
private var _Wikipedia: ImageVector? = null
