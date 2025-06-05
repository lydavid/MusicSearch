package ly.david.musicsearch.shared.feature.details.place

import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.place.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.DetailsPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.EntitiesListPresenter
import ly.david.musicsearch.ui.common.topappbar.Tab

internal val placeTabs = persistentListOf(
    Tab.DETAILS,

    // TODO: Should exclude event-rels because they appear to be the same as the results from browse events by place
    Tab.RELATIONSHIPS,
    Tab.EVENTS,
    Tab.STATS,
)

internal class PlacePresenter(
    screen: DetailsScreen,
    navigator: Navigator,
    private val repository: PlaceRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    entitiesListPresenter: EntitiesListPresenter,
    imageMetadataRepository: ImageMetadataRepository,
    logger: Logger,
    loginPresenter: LoginPresenter,
    getMusicBrainzUrl: GetMusicBrainzUrl,
    wikimediaRepository: WikimediaRepository,
) : DetailsPresenter<PlaceDetailsModel>(
    screen = screen,
    navigator = navigator,
    incrementLookupHistory = incrementLookupHistory,
    entitiesListPresenter = entitiesListPresenter,
    imageMetadataRepository = imageMetadataRepository,
    logger = logger,
    loginPresenter = loginPresenter,
    getMusicBrainzUrl = getMusicBrainzUrl,
    wikimediaRepository = wikimediaRepository,
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
        )
    }
}
