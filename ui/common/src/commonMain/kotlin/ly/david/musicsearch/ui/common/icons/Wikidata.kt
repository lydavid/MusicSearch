package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// https://commons.wikimedia.org/wiki/File:Wikidata.svg
val CustomIcons.Wikidata: ImageVector
    get() {
        if (_Wikidata != null) {
            return _Wikidata!!
        }
        _Wikidata = ImageVector.Builder(
            name = "Wikidata",
            defaultWidth = 1050.dp,
            defaultHeight = 590.dp,
            viewportWidth = 1050f,
            viewportHeight = 590f,
        ).apply {
            path(fill = SolidColor(Color(0xFF990000))) {
                moveToRelative(120f, 545f)
                horizontalLineToRelative(30f)
                lineTo(150f, 45f)
                lineTo(120f, 45f)
                lineTo(120f, 545f)
                close()
                moveTo(180f, 545f)
                horizontalLineToRelative(90f)
                lineTo(270f, 45f)
                lineTo(180f, 45f)
                lineTo(180f, 545f)
                close()
                moveTo(300f, 45f)
                lineTo(300f, 545f)
                horizontalLineToRelative(90f)
                lineTo(390f, 45f)
                horizontalLineToRelative(-90f)
                close()
            }
            path(fill = SolidColor(Color(0xFF339966))) {
                moveToRelative(840f, 545f)
                horizontalLineToRelative(30f)
                verticalLineTo(45f)
                horizontalLineTo(840f)
                verticalLineTo(545f)
                close()
                moveTo(900f, 45f)
                verticalLineTo(545f)
                horizontalLineToRelative(30f)
                verticalLineTo(45f)
                horizontalLineTo(900f)
                close()
                moveTo(420f, 545f)
                horizontalLineToRelative(30f)
                verticalLineTo(45f)
                horizontalLineTo(420f)
                verticalLineTo(545f)
                close()
                moveTo(480f, 45f)
                verticalLineTo(545f)
                horizontalLineToRelative(30f)
                verticalLineTo(45f)
                horizontalLineToRelative(-30f)
                close()
            }
            path(fill = SolidColor(Color(0xFF006699))) {
                moveToRelative(540f, 545f)
                horizontalLineToRelative(90f)
                lineTo(630f, 45f)
                horizontalLineToRelative(-90f)
                lineTo(540f, 545f)
                close()
                moveTo(660f, 545f)
                horizontalLineToRelative(30f)
                lineTo(690f, 45f)
                lineTo(660f, 45f)
                lineTo(660f, 545f)
                close()
                moveTo(720f, 45f)
                lineTo(720f, 545f)
                horizontalLineToRelative(90f)
                lineTo(810f, 45f)
                lineTo(720f, 45f)
                close()
            }
        }.build()

        return _Wikidata!!
    }

@Suppress("ObjectPropertyName")
private var _Wikidata: ImageVector? = null
