package ly.david.musicsearch.shared.feature.listens.submit

import ly.david.musicsearch.shared.domain.listen.TrackInfo

internal fun List<TrackInfo>.getTotalListenLength(): Long {
    return sumOf { it.nonZeroLengthMilliseconds }
}
