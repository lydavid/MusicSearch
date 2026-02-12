package ly.david.musicsearch.ui.common.release

import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.bootleg
import musicsearch.ui.common.generated.resources.cancelled
import musicsearch.ui.common.generated.resources.expunged
import musicsearch.ui.common.generated.resources.official
import musicsearch.ui.common.generated.resources.promotion
import musicsearch.ui.common.generated.resources.pseudoRelease
import musicsearch.ui.common.generated.resources.withdrawn
import org.jetbrains.compose.resources.StringResource

fun ReleaseStatus.getDisplayString(): StringResource {
    return when (this) {
        ReleaseStatus.OFFICIAL -> Res.string.official
        ReleaseStatus.PROMOTION -> Res.string.promotion
        ReleaseStatus.BOOTLEG -> Res.string.bootleg
        ReleaseStatus.PSEUDO_RELEASE -> Res.string.pseudoRelease
        ReleaseStatus.WITHDRAWN -> Res.string.withdrawn
        ReleaseStatus.EXPUNGED -> Res.string.expunged
        ReleaseStatus.CANCELLED -> Res.string.cancelled
    }
}
