package ly.david.musicsearch.shared.feature.details.releasegroup

import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.DetailsPresenter
import ly.david.musicsearch.ui.common.list.AllEntitiesListPresenter
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginPresenter
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Clock

internal val releaseGroupTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELEASES,
    Tab.RELATIONSHIPS,
)

internal class ReleaseGroupPresenter(
    screen: DetailsScreen,
    navigator: Navigator,
    private val repository: ReleaseGroupRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    allEntitiesListPresenter: AllEntitiesListPresenter,
    imageMetadataRepository: ImageMetadataRepository,
    logger: Logger,
    musicBrainzLoginPresenter: MusicBrainzLoginPresenter,
    getMusicBrainzUrl: GetMusicBrainzUrl,
    wikimediaRepository: WikimediaRepository,
    collectionRepository: CollectionRepository,
) : DetailsPresenter<ReleaseGroupDetailsModel>(
    screen = screen,
    navigator = navigator,
    incrementLookupHistory = incrementLookupHistory,
    allEntitiesListPresenter = allEntitiesListPresenter,
    imageMetadataRepository = imageMetadataRepository,
    logger = logger,
    musicBrainzLoginPresenter = musicBrainzLoginPresenter,
    getMusicBrainzUrl = getMusicBrainzUrl,
    wikimediaRepository = wikimediaRepository,
    collectionRepository = collectionRepository,
) {

    override fun getTabs(): ImmutableList<Tab> {
        return releaseGroupTabs
    }

    override fun getSubtitle(detailsModel: ReleaseGroupDetailsModel): String {
        return "Release Group by ${detailsModel.artistCredits.getDisplayNames()}"
    }

    override suspend fun lookupDetailsModel(
        id: String,
        forceRefresh: Boolean,
    ): ReleaseGroupDetailsModel {
        return repository.lookupReleaseGroup(
            releaseGroupId = id,
            forceRefresh = forceRefresh,
            lastUpdated = Clock.System.now(),
        )
    }
}
