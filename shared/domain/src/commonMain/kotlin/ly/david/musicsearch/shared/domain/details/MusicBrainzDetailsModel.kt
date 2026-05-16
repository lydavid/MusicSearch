package ly.david.musicsearch.shared.domain.details

import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.MusicBrainzModel
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip
import kotlin.time.Instant

sealed interface MusicBrainzDetailsModel : MusicBrainzModel, NameWithDisambiguationAndAliases {
    val lastUpdated: Instant
    val artistCredits: ImmutableList<ArtistCreditUiModel>
    val genres: ImmutableList<GenreChip>
    val tags: ImmutableList<TagChip>
    val urls: ImmutableList<RelationListItemModel>
    override val aliases: ImmutableList<BasicAlias>

    fun withArtistCredits(artistCredits: ImmutableList<ArtistCreditUiModel>): MusicBrainzDetailsModel
    fun withUrls(urls: ImmutableList<RelationListItemModel>): MusicBrainzDetailsModel
    override fun withAliases(aliases: ImmutableList<BasicAlias>): MusicBrainzDetailsModel
}

fun MusicBrainzDetailsModel.asEntity(): MusicBrainzEntity {
    return MusicBrainzEntity(
        id = id,
        type = when (this) {
            is AreaDetailsModel -> MusicBrainzEntityType.AREA
            is ArtistDetailsModel -> MusicBrainzEntityType.ARTIST
            is EventDetailsModel -> MusicBrainzEntityType.EVENT
            is InstrumentDetailsModel -> MusicBrainzEntityType.INSTRUMENT
            is LabelDetailsModel -> MusicBrainzEntityType.LABEL
            is PlaceDetailsModel -> MusicBrainzEntityType.PLACE
            is RecordingDetailsModel -> MusicBrainzEntityType.RECORDING
            is ReleaseDetailsModel -> MusicBrainzEntityType.RELEASE
            is ReleaseGroupDetailsModel -> MusicBrainzEntityType.RELEASE_GROUP
            is SeriesDetailsModel -> MusicBrainzEntityType.SERIES
            is WorkDetailsModel -> MusicBrainzEntityType.WORK
        },
    )
}
