package ly.david.musicsearch.domain.nowplaying.usecase

import ly.david.musicsearch.domain.nowplaying.NowPlayingHistoryRepository
import org.koin.core.annotation.Single

@Single
class DeleteNowPlayingHistory(
    private val nowPlayingHistoryRepository: NowPlayingHistoryRepository,
) {
    operator fun invoke(
        id: String,
    ) = nowPlayingHistoryRepository.delete(raw = id)
}
