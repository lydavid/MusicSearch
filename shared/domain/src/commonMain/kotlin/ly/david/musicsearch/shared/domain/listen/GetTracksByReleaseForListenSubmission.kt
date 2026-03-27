package ly.david.musicsearch.shared.domain.listen

interface GetTracksByReleaseForListenSubmission {
    operator fun invoke(releaseId: String): List<TrackInfo>
}
