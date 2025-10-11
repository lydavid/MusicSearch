package ly.david.musicsearch.shared.feature.details.place

import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.DetailsPresenter
import ly.david.musicsearch.ui.common.list.AllEntitiesListPresenter
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginPresenter
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Clock

internal val placeTabs = persistentListOf(
    Tab.DETAILS,

    // TODO: Should exclude event-rels because they appear to be the same as the results from browse events by place
    Tab.RELATIONSHIPS,
    Tab.EVENTS,
)

internal class PlacePresenter(
    screen: DetailsScreen,
    navigator: Navigator,
    private val repository: PlaceRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    allEntitiesListPresenter: AllEntitiesListPresenter,
    imageMetadataRepository: ImageMetadataRepository,
    logger: Logger,
    musicBrainzLoginPresenter: MusicBrainzLoginPresenter,
    getMusicBrainzUrl: GetMusicBrainzUrl,
    wikimediaRepository: WikimediaRepository,
    collectionRepository: CollectionRepository,
) : DetailsPresenter<PlaceDetailsModel>(
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
        return placeTabs
    }

    override suspend fun lookupDetailsModel(
        id: String,
        forceRefresh: Boolean,
    ): PlaceDetailsModel {
        return repository.lookupPlace(
            placeId = id,
            forceRefresh = forceRefresh,
            lastUpdated = Clock.System.now(),
        )
    }
}
