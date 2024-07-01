package ly.david.musicsearch.ui.core.preview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
)
@Preview(
    name = "Phone",
    device = Devices.PHONE,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Unfolded Foldable",
    device = Devices.FOLDABLE,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Tablet",
    device = Devices.TABLET,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Desktop",
    device = Devices.DESKTOP,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Phone",
    device = Devices.PHONE,
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Unfolded Foldable",
    device = Devices.FOLDABLE,
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Tablet",
    device = Devices.TABLET,
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Desktop",
    device = Devices.DESKTOP,
    showSystemUi = true,
    group = "Light",
)
actual annotation class DefaultPreviews
