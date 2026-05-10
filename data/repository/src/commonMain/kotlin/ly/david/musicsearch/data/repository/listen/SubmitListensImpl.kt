package ly.david.musicsearch.data.repository.listen

import kotlinx.coroutines.flow.first
import ly.david.musicsearch.shared.domain.app.AppInfo
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listen.ListenSubmission
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listen.SubmitListenFeedback
import ly.david.musicsearch.shared.domain.listen.SubmitListens
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

class SubmitListensImpl(
    private val appPreferences: AppPreferences,
    private val appInfo: AppInfo,
    private val listensListRepository: ListensListRepository,
) : SubmitListens {
    override suspend operator fun invoke(
        username: String,
        listenSubmissions: List<ListenSubmission>,
    ): Feedback<SubmitListenFeedback> {
        val submitClientAndVersionWithListen = appPreferences.submitClientAndVersionWithListen.first()

        return listensListRepository.submitListens(
            username = username,
            listenSubmissions = listenSubmissions,
            appInfo = appInfo.takeIf { submitClientAndVersionWithListen },
        )
    }
}
