package ly.david.musicsearch.shared.feature.details.recording

import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.recording.RecordingRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.DetailsPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.list.AllEntitiesListPresenter
import ly.david.musicsearch.ui.common.topappbar.Tab

internal val recordingTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELEASES,
    Tab.RELATIONSHIPS,
    Tab.STATS,
)

internal class RecordingPresenter(
    screen: DetailsScreen,
    navigator: Navigator,
    private val repository: RecordingRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    allEntitiesListPresenter: AllEntitiesListPresenter,
    imageMetadataRepository: ImageMetadataRepository,
    logger: Logger,
    loginPresenter: LoginPresenter,
    getMusicBrainzUrl: GetMusicBrainzUrl,
    wikimediaRepository: WikimediaRepository,
    collectionRepository: CollectionRepository,
) : DetailsPresenter<RecordingDetailsModel>(
    screen = screen,
    navigator = navigator,
    incrementLookupHistory = incrementLookupHistory,
    allEntitiesListPresenter = allEntitiesListPresenter,
    imageMetadataRepository = imageMetadataRepository,
    logger = logger,
    loginPresenter = loginPresenter,
    getMusicBrainzUrl = getMusicBrainzUrl,
    wikimediaRepository = wikimediaRepository,
    collectionRepository = collectionRepository,
) {
    override fun getTabs(): ImmutableList<Tab> {
        return recordingTabs
    }

    override fun getSubtitle(detailsModel: RecordingDetailsModel): String {
        return "Recording by ${detailsModel.artistCredits.getDisplayNames()}"
    }

    override suspend fun lookupDetailsModel(
        id: String,
        forceRefresh: Boolean,
    ): RecordingDetailsModel {
        return repository.lookupRecording(
            recordingId = id,
            forceRefresh = forceRefresh,
            lastUpdated = Clock.System.now(),
        )
    }
}
