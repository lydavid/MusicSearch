package ly.david.musicsearch.shared.feature.details.releasegroup

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.DetailsPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.EntitiesListPresenter
import ly.david.musicsearch.ui.common.topappbar.Tab

internal val releaseGroupTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELEASES,
    Tab.RELATIONSHIPS,
    Tab.STATS,
)

internal class ReleaseGroupPresenter(
    screen: DetailsScreen,
    navigator: Navigator,
    private val repository: ReleaseGroupRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    entitiesListPresenter: EntitiesListPresenter,
    relationsPresenter: RelationsPresenter,
    imageMetadataRepository: ImageMetadataRepository,
    logger: Logger,
    loginPresenter: LoginPresenter,
    getMusicBrainzUrl: GetMusicBrainzUrl,
    wikimediaRepository: WikimediaRepository,
) : DetailsPresenter<ReleaseGroupDetailsModel>(
    screen = screen,
    navigator = navigator,
    incrementLookupHistory = incrementLookupHistory,
    entitiesListPresenter = entitiesListPresenter,
    relationsPresenter = relationsPresenter,
    imageMetadataRepository = imageMetadataRepository,
    logger = logger,
    loginPresenter = loginPresenter,
    getMusicBrainzUrl = getMusicBrainzUrl,
    wikimediaRepository = wikimediaRepository,
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
        )
    }
}

internal sealed interface ReleaseGroupUiEvent : CircuitUiEvent {
    data object NavigateUp : ReleaseGroupUiEvent
    data object ForceRefreshDetails : ReleaseGroupUiEvent
    data class UpdateTab(val tab: Tab) : ReleaseGroupUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ReleaseGroupUiEvent

    data object ClickImage : ReleaseGroupUiEvent

    data object ToggleCollapseExpandExternalLinks : ReleaseGroupUiEvent
}
