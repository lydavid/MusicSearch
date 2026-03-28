package ly.david.musicsearch.ui.common.artist

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.artist.ArtistType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.character
import musicsearch.ui.common.generated.resources.choir
import musicsearch.ui.common.generated.resources.group
import musicsearch.ui.common.generated.resources.orchestra
import musicsearch.ui.common.generated.resources.other
import musicsearch.ui.common.generated.resources.person
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtistType.getDisplayString(): String {
    return stringResource(
        when (this) {
            ArtistType.CHARACTER -> Res.string.character
            ArtistType.CHOIR -> Res.string.choir
            ArtistType.GROUP -> Res.string.group
            ArtistType.ORCHESTRA -> Res.string.orchestra
            ArtistType.OTHER -> Res.string.other
            ArtistType.PERSON -> Res.string.person
        },
    )
}
