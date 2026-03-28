package ly.david.musicsearch.ui.common.artist

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.artist.ArtistGender
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.female
import musicsearch.ui.common.generated.resources.male
import musicsearch.ui.common.generated.resources.nonBinary
import musicsearch.ui.common.generated.resources.notApplicable
import musicsearch.ui.common.generated.resources.other
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtistGender.getDisplayString(): String {
    return stringResource(
        when (this) {
            ArtistGender.FEMALE -> Res.string.female
            ArtistGender.MALE -> Res.string.male
            ArtistGender.NON_BINARY -> Res.string.nonBinary
            ArtistGender.NOT_APPLICABLE -> Res.string.notApplicable
            ArtistGender.OTHER -> Res.string.other
        },
    )
}
