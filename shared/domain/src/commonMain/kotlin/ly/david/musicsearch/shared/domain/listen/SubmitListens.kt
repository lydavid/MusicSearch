package ly.david.musicsearch.shared.domain.listen

import ly.david.musicsearch.shared.domain.error.Feedback

interface SubmitListens {
    suspend operator fun invoke(
        username: String,
        listenSubmissions: List<ListenSubmission>,
    ): Feedback<SubmitListenFeedback>
}
