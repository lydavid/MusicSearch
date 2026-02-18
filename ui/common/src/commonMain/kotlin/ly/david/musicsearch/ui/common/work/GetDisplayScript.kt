package ly.david.musicsearch.ui.common.work

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.musicbrainz.MULTIPLE_SCRIPT_CODE
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.multipleScripts
import org.jetbrains.compose.resources.stringResource

@Composable
fun String.getDisplayScript(): String? {
    return when (this) {
        MULTIPLE_SCRIPT_CODE -> stringResource(Res.string.multipleScripts)
        else -> getPlatformDisplayScript()
    }
}

internal expect fun String.getPlatformDisplayScript(): String?
