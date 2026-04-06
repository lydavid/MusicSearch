package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupPrimaryType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSecondaryType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypes
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.album
import musicsearch.ui.common.generated.resources.audioDrama
import musicsearch.ui.common.generated.resources.audiobook
import musicsearch.ui.common.generated.resources.broadcast
import musicsearch.ui.common.generated.resources.compilation
import musicsearch.ui.common.generated.resources.demo
import musicsearch.ui.common.generated.resources.djMix
import musicsearch.ui.common.generated.resources.ep
import musicsearch.ui.common.generated.resources.fieldRecording
import musicsearch.ui.common.generated.resources.interview
import musicsearch.ui.common.generated.resources.live
import musicsearch.ui.common.generated.resources.mixtapeStreet
import musicsearch.ui.common.generated.resources.other
import musicsearch.ui.common.generated.resources.remix
import musicsearch.ui.common.generated.resources.single
import musicsearch.ui.common.generated.resources.soundtrack
import musicsearch.ui.common.generated.resources.spokenword
import musicsearch.ui.common.generated.resources.unspecifiedType
import org.jetbrains.compose.resources.stringResource

/**
 * Returns primary type concatenated with all secondary types for display.
 */
@Composable
fun ReleaseGroupTypes.getDisplayString(): String {
    var displayTypes = primaryType?.getDisplayString().orEmpty()

    if (displayTypes.isNotEmpty() && secondaryTypes.isNotEmpty()) {
        displayTypes += " + "
    }
    displayTypes += secondaryTypes.map { it.getDisplayString() }.joinToString(separator = " + ")

    return displayTypes.ifEmpty { stringResource(Res.string.unspecifiedType) }
}

@Composable
private fun ReleaseGroupPrimaryType.getDisplayString(): String {
    return stringResource(
        when (this) {
            ReleaseGroupPrimaryType.Album -> Res.string.album
            ReleaseGroupPrimaryType.Broadcast -> Res.string.broadcast
            ReleaseGroupPrimaryType.EP -> Res.string.ep
            ReleaseGroupPrimaryType.Other -> Res.string.other
            ReleaseGroupPrimaryType.Single -> Res.string.single
        },
    )
}

@Composable
private fun ReleaseGroupSecondaryType.getDisplayString(): String {
    return stringResource(
        when (this) {
            ReleaseGroupSecondaryType.AudioDrama -> Res.string.audioDrama
            ReleaseGroupSecondaryType.Audiobook -> Res.string.audiobook
            ReleaseGroupSecondaryType.Compilation -> Res.string.compilation
            ReleaseGroupSecondaryType.DJMix -> Res.string.djMix
            ReleaseGroupSecondaryType.Demo -> Res.string.demo
            ReleaseGroupSecondaryType.FieldRecording -> Res.string.fieldRecording
            ReleaseGroupSecondaryType.Interview -> Res.string.interview
            ReleaseGroupSecondaryType.Live -> Res.string.live
            ReleaseGroupSecondaryType.MixtapeStreet -> Res.string.mixtapeStreet
            ReleaseGroupSecondaryType.Remix -> Res.string.remix
            ReleaseGroupSecondaryType.Soundtrack -> Res.string.soundtrack
            ReleaseGroupSecondaryType.Spokenword -> Res.string.spokenword
        },
    )
}
