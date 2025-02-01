package ly.david.musicsearch.ui.core.preview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes

/**
 * A copy of [PreviewScreenSizes] with light and dark mode.
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
)
@Preview(
    name = "Phone",
    device = "spec:width=411dp, height=891dp",
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width=411dp, height=891dp, orientation=landscape, dpi=420",
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Unfolded Foldable",
    device = "spec:width=673dp, height=841dp",
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp, height=800dp,dpi=240",
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Desktop",
    device = "spec:width=1920dp, height=1080dp,dpi=160",
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark",
)
@Preview(
    name = "Phone",
    device = "spec:width=411dp, height=891dp",
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width=411dp, height=891dp, orientation=landscape, dpi=420",
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Unfolded Foldable",
    device = "spec:width=673dp, height=841dp",
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp, height=800dp,dpi=240",
    showSystemUi = true,
    group = "Light",
)
@Preview(
    name = "Desktop",
    device = "spec:width=1920dp, height=1080dp,dpi=160",
    showSystemUi = true,
    group = "Light",
)
actual annotation class DefaultPreviews
