package ly.david.data

import ly.david.data.network.MusicBrainzModel

/**
 * Not exclusive to [MusicBrainzModel].
 * This just means an id belongs to MusicBrainz.
 */
interface MusicBrainzId {
    val id: String
}
