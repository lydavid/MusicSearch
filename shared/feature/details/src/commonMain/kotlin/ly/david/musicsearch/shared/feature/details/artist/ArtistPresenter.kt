package ly.david.musicsearch.shared.feature.details.artist

import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Clock
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.DetailsPresenter
import ly.david.musicsearch.ui.common.artist.artistTabs
import ly.david.musicsearch.ui.common.list.AllEntitiesListPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab

internal class ArtistPresenter(
    screen: DetailsScreen,
    navigator: Navigator,
    private val repository: ArtistRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    allEntitiesListPresenter: AllEntitiesListPresenter,
    imageMetadataRepository: ImageMetadataRepository,
    logger: Logger,
    loginPresenter: LoginPresenter,
    getMusicBrainzUrl: GetMusicBrainzUrl,
    wikimediaRepository: WikimediaRepository,
    collectionRepository: CollectionRepository,
) : DetailsPresenter<ArtistDetailsModel>(
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
        return artistTabs
    }

    override fun getSearchHint(detailsModel: ArtistDetailsModel?): String? {
        return detailsModel?.sortName
    }

    override suspend fun lookupDetailsModel(
        id: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel {
        return repository.lookupArtist(
            artistId = id,
            forceRefresh = forceRefresh,
            lastUpdated = Clock.System.now(),
        )
    }
}
