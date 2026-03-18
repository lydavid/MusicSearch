package ly.david.musicsearch.data.database.mapper

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.database.GROUP_CONCAT_DELIMITER
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel

internal fun combineToArtistCredits(
    names: String?,
    ids: String?,
    joinPhrases: String?,
): ImmutableList<ArtistCreditUiModel> {
    return if (names != null && ids != null) {
        val nameList = names.split(GROUP_CONCAT_DELIMITER)
        val idList = ids.split(GROUP_CONCAT_DELIMITER)
        // on some older SDKs with older SQLite versions, we may get null instead of an empty string from GROUP_CONCAT
        val joinPhraseList = joinPhrases?.split(GROUP_CONCAT_DELIMITER)
        nameList.indices.map { i ->
            ArtistCreditUiModel(
                name = nameList[i],
                artistId = idList[i],
                joinPhrase = joinPhraseList?.get(i).orEmpty(),
            )
        }
    } else {
        emptyList()
    }.toPersistentList()
}
