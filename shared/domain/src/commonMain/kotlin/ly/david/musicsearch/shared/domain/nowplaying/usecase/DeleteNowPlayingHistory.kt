package ly.david.musicsearch.shared.domain.nowplaying.usecase

import ly.david.musicsearch.shared.domain.nowplaying.NowPlayingHistoryRepository
import org.koin.core.annotation.Single

@Single
class DeleteNowPlayingHistory(
    private val nowPlayingHistoryRepository: NowPlayingHistoryRepository,
) {
    operator fun invoke(
        id: String,
    ) = nowPlayingHistoryRepository.delete(raw = id)
}
