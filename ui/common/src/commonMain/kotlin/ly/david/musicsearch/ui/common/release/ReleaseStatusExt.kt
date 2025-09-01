package ly.david.musicsearch.ui.common.release

import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.strings.AppStrings

fun ReleaseStatus.getDisplayString(strings: AppStrings): String {
    return when (this) {
        ReleaseStatus.OFFICIAL -> strings.official
        ReleaseStatus.PROMOTION -> strings.promotion
        ReleaseStatus.BOOTLEG -> strings.bootleg
        ReleaseStatus.PSEUDO_RELEASE -> strings.pseudoRelease
        ReleaseStatus.WITHDRAWN -> strings.withdrawn
        ReleaseStatus.EXPUNGED -> strings.expunged
        ReleaseStatus.CANCELLED -> strings.cancelled
    }
}
