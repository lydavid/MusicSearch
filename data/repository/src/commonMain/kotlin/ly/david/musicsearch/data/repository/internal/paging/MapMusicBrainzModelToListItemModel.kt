package ly.david.musicsearch.data.repository.internal.paging

import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkAttributeMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel

/**
 * Converts a [MusicBrainzModel] that we got from the network to its UI version for display.
 *
 * We can map a [MusicBrainzModel] to [ListItemModel] but not the other way around
 * because there are [ListItemModel] such as [EndOfList] that do not have a 1-to-1 mapping.
 * We could still do it, but the result will be nullable.
 */
internal fun MusicBrainzModel.toListItemModel(): ListItemModel {
    return when (this) {
        is ArtistMusicBrainzModel -> this.toArtistListItemModel()
        is ReleaseGroupMusicBrainzModel -> this.toReleaseGroupListItemModel()
        is ReleaseMusicBrainzModel -> this.toReleaseListItemModel()
        is RecordingMusicBrainzModel -> this.toRecordingListItemModel()
        is PlaceMusicBrainzModel -> this.toPlaceListItemModel()
        is AreaMusicBrainzModel -> this.toAreaListItemModel()
        is InstrumentMusicBrainzModel -> this.toInstrumentListItemModel()
        is LabelMusicBrainzModel -> this.toLabelListItemModel()
        is WorkMusicBrainzModel -> this.toWorkListItemModel()
        is EventMusicBrainzModel -> this.toEventListItemModel()
        is SeriesMusicBrainzModel -> this.toSeriesListItemModel()
        is GenreMusicBrainzModel -> this.toGenreListItemModel()
        is CollectionMusicBrainzModel -> error(
            "Converting collection MusicBrainz models directly to list item models not supported.",
        )
    }
}

private fun AreaMusicBrainzModel.toAreaListItemModel(date: String? = null) = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    countryCodes = countryCodes,
    date = date,
)

private fun ArtistMusicBrainzModel.toArtistListItemModel() =
    ArtistListItemModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    )

private fun EventMusicBrainzModel.toEventListItemModel() =
    EventListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    )

private fun InstrumentMusicBrainzModel.toInstrumentListItemModel() =
    InstrumentListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )

private fun LabelMusicBrainzModel.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
    )

private fun PlaceMusicBrainzModel.toPlaceListItemModel() =
    PlaceListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan?.toLifeSpanUiModel(),
        area = area?.toAreaListItemModel(),
    )

private fun RecordingMusicBrainzModel.toRecordingListItemModel() = RecordingListItemModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    length = length,
    video = video ?: false,
    formattedArtistCredits = artistCredits.getDisplayNames(),
)

private fun ReleaseGroupMusicBrainzModel.toReleaseGroupListItemModel(): ReleaseGroupListItemModel {
    return ReleaseGroupListItemModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        formattedArtistCredits = artistCredits.getDisplayNames(),
    )
}

private fun ReleaseMusicBrainzModel.toReleaseListItemModel() = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    countryCode = countryCode,
    imageUrl = null,
    formattedArtistCredits = artistCredits.getDisplayNames(),
    releaseCountryCount = releaseEvents?.count() ?: 0,
)

private fun SeriesMusicBrainzModel.toSeriesListItemModel() =
    SeriesListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )

private fun WorkMusicBrainzModel.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = attributes?.map { it.toWorkAttributeUiModel() }.orEmpty(),
    )

private fun LifeSpanMusicBrainzModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin,
    end = end,
    ended = ended,
)

private fun WorkAttributeMusicBrainzModel.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = typeId,
        value = value,
    )

private fun GenreMusicBrainzModel.toGenreListItemModel() =
    GenreListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
    )
