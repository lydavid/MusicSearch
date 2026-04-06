package ly.david.musicsearch.shared.domain.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroup
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupPrimaryType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSecondaryType
import kotlin.time.Clock
import kotlin.time.Instant

data class ReleaseGroupDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val firstReleaseDate: String = "",
    override val primaryType: ReleaseGroupPrimaryType? = null,
    override val secondaryTypes: ImmutableList<ReleaseGroupSecondaryType> = persistentListOf(),
    override val lastUpdated: Instant = Clock.System.now(),
    override val artistCredits: ImmutableList<ArtistCreditUiModel> = persistentListOf(),
    override val urls: ImmutableList<RelationListItemModel> = persistentListOf(),
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : ReleaseGroup, MusicBrainzDetailsModel {
    override fun withArtistCredits(artistCredits: ImmutableList<ArtistCreditUiModel>): MusicBrainzDetailsModel {
        return copy(artistCredits = artistCredits)
    }

    override fun withUrls(urls: ImmutableList<RelationListItemModel>): MusicBrainzDetailsModel {
        return copy(urls = urls)
    }

    override fun withAliases(aliases: ImmutableList<BasicAlias>): MusicBrainzDetailsModel {
        return copy(aliases = aliases)
    }
}
