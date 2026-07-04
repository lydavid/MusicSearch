package ly.david.musicsearch.data.database.mapper

import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel

internal fun mapToRecordingListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    firstReleaseDate: String,
    length: Int?,
    video: Boolean,
    isrcs: List<String>,
    formattedArtistCreditNames: String,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
    listenCount: Long?,
    lastListenedAtMs: Long?,
) = RecordingListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    length = length,
    video = video,
    formattedArtistCredits = formattedArtistCreditNames,
    isrcs = isrcs.toPersistentList(),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
    listenCount = listenCount,
    lastListenedAtMs = lastListenedAtMs,
)
