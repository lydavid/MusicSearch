package ly.david.musicsearch.shared.domain.nowplaying.usecase

import ly.david.musicsearch.shared.domain.nowplaying.NowPlayingHistoryRepository
import org.koin.core.annotation.Single

@Single
class GetNowPlayingHistory(
    private val nowPlayingHistoryRepository: NowPlayingHistoryRepository,
) {
    operator fun invoke(
        query: String,
    ) = nowPlayingHistoryRepository.observeNowPlayingHistory(query)
}
