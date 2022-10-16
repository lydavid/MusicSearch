package ly.david.mbjc.data.network

import androidx.annotation.StringRes
import com.squareup.moshi.Json
import ly.david.mbjc.R

internal const val RESOURCE_AREA = "area"
internal const val RESOURCE_ARTIST = "artist"
internal const val RESOURCE_EVENT = "event"
internal const val RESOURCE_GENRE = "genre"
internal const val RESOURCE_INSTRUMENT = "instrument"
internal const val RESOURCE_LABEL = "label"
internal const val RESOURCE_PLACE = "place"
internal const val RESOURCE_RECORDING = "recording"
internal const val RESOURCE_RELEASE = "release"
internal const val RESOURCE_RELEASE_GROUP = "release-group"
internal const val RESOURCE_SERIES = "series"
internal const val RESOURCE_WORK = "work"
internal const val RESOURCE_URL = "url"

/**
 * These are resources available for lookup requests. Many of them are query-able as well.
 *
 * @param resourceName The resource to use for query/lookup request to MusicBrainz.
 */
internal enum class MusicBrainzResource(@StringRes val displayTextRes: Int, val resourceName: String) {
    @Json(name = "area")
    AREA(R.string.area, RESOURCE_AREA),

    @Json(name = "artist")
    ARTIST(R.string.artist, RESOURCE_ARTIST),

    @Json(name = "event")
    EVENT(R.string.event, RESOURCE_EVENT),

    // Not searchable, but lookupable
    @Json(name = "genre")
    GENRE(R.string.genre, RESOURCE_GENRE),

    @Json(name = "instrument")
    INSTRUMENT(R.string.instrument, RESOURCE_INSTRUMENT),

    @Json(name = "label")
    LABEL(R.string.label, RESOURCE_LABEL),

    @Json(name = "place")
    PLACE(R.string.place, RESOURCE_PLACE),

    @Json(name = "recording")
    RECORDING(R.string.recording, RESOURCE_RECORDING),

    @Json(name = "release")
    RELEASE(R.string.release, RESOURCE_RELEASE),

    // Note that target-type uses release_group, while uri uses release-group.
    // For our internal resource, we will use release-group.
    @Json(name = "release_group")
    RELEASE_GROUP(R.string.release_group, RESOURCE_RELEASE_GROUP),

    @Json(name = "series")
    SERIES(R.string.series, RESOURCE_SERIES),

    @Json(name = "work")
    WORK(R.string.work, RESOURCE_WORK),

    // Not searchable, but lookupable
    @Json(name = "url")
    URL(R.string.url, RESOURCE_URL),

    // Other searchable: annotation, tag, cd stub, editor, documentation

    // Other non-core resources with API: rating, tag, collection

    // Other lookupable: discid, isrc, iswc
}

internal val searchableResources: List<MusicBrainzResource>
    get() = listOf(
        MusicBrainzResource.AREA,
        MusicBrainzResource.ARTIST,
        MusicBrainzResource.EVENT,
        MusicBrainzResource.INSTRUMENT,
        MusicBrainzResource.LABEL,
        MusicBrainzResource.PLACE,
        MusicBrainzResource.RECORDING,
        MusicBrainzResource.RELEASE,
        MusicBrainzResource.RELEASE_GROUP,
        MusicBrainzResource.SERIES,
        MusicBrainzResource.WORK,
    )

internal fun String.toMusicBrainzResource(): MusicBrainzResource? =
    MusicBrainzResource.values().firstOrNull { this == it.resourceName }
