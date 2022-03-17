package ly.david.mbjc.ui.releasegroup

// TODO: mediator, can we generalize?
//@HiltViewModel
//class OldReleasesByReleaseGroupViewModel @Inject constructor(
//    private val musicBrainzApiService: MusicBrainzApiService
//): ViewModel() {
//
//    private var initialized = false
//
//    private val allReleases = mutableListOf<MusicBrainzRelease>()
//
//    // TODO: page, find release group with more than 100 releases
//    suspend fun getReleasesByReleaseGroup(
//        releaseGroupId: String,
//        limit: Int = BROWSE_LIMIT,
//        offset: Int = 0
//    ): List<MusicBrainzRelease> {
//        if (initialized) {
//            return allReleases
//        }
//        if (offset != 0) {
//            delay(DELAY_PAGED_API_CALLS_MS)
//        }
//        val response = musicBrainzApiService.browseReleasesByReleaseGroup(
//            releaseGroupId = releaseGroupId,
//            limit = limit,
//            offset = offset
//        )
//
//        val newReleases = response.musicBrainzReleases
//        allReleases.addAll(newReleases)
//        return if (allReleases.size < response.count) {
//            getReleasesByReleaseGroup(
//                releaseGroupId = releaseGroupId,
//                offset = offset + newReleases.size
//            )
//        } else {
//            initialized = true
//            allReleases
//        }
//    }
//}
