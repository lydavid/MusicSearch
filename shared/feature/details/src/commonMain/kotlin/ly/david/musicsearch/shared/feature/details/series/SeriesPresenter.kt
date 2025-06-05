package ly.david.musicsearch.shared.feature.details.series

import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.DetailsPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.EntitiesListPresenter
import ly.david.musicsearch.ui.common.topappbar.Tab

internal val seriesTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELATIONSHIPS,
    Tab.STATS,
)

internal class SeriesPresenter(
    screen: DetailsScreen,
    navigator: Navigator,
    private val repository: SeriesRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    entitiesListPresenter: EntitiesListPresenter,
    imageMetadataRepository: ImageMetadataRepository,
    logger: Logger,
    loginPresenter: LoginPresenter,
    getMusicBrainzUrl: GetMusicBrainzUrl,
    wikimediaRepository: WikimediaRepository,
) : DetailsPresenter<SeriesDetailsModel>(
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
        return seriesTabs
    }

    override suspend fun lookupDetailsModel(
        id: String,
        forceRefresh: Boolean,
    ): SeriesDetailsModel {
        return repository.lookupSeries(
            seriesId = id,
            forceRefresh = forceRefresh,
        )
    }
}
