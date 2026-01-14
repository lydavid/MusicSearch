package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.history.NowPlayingHistory
import kotlin.time.Clock
import kotlin.time.Instant

data class NowPlayingHistoryListItemModel(
    override val id: String,
    val title: String,
    val artist: String,
    val lastPlayed: Instant = Clock.System.now(),
) : ListItemModel

private const val EN_DELIMITER = " by "

@Suppress("ReturnCount")
private fun splitEnglishBy(raw: String): Pair<String, String>? {
    val index = raw.lastIndexOf(EN_DELIMITER)
    if (index == -1) return null

    val title = raw.take(index).trim()
    val artist = raw.substring(index + EN_DELIMITER.length).trim()

    if (title.isEmpty() || artist.isEmpty()) return null
    return title to artist
}

private val dePattern = Regex("""^„(.+?)“ von (.+)$""")
private val zhPattern = Regex("""^(.+?)的《(.+?)》$""")
private val jaPattern = Regex("""^(.+?)（(.+?)）$""")

@Suppress("ReturnCount")
private fun parseTitleArtist(raw: String): Pair<String, String> {
    dePattern.find(raw)?.let {
        return it.groupValues[1] to it.groupValues[2]
    }

    zhPattern.find(raw)?.let {
        return it.groupValues[2] to it.groupValues[1]
    }

    jaPattern.find(raw)?.let {
        return it.groupValues[1] to it.groupValues[2]
    }

    splitEnglishBy(raw)?.let { return it }

    return raw to ""
}

fun NowPlayingHistory.toNowPlayingHistoryListItemModel(): NowPlayingHistoryListItemModel {
    val (title, artist) = parseTitleArtist(raw)

    return NowPlayingHistoryListItemModel(
        id = raw,
        title = title,
        artist = artist,
        lastPlayed = lastPlayed,
    )
}
