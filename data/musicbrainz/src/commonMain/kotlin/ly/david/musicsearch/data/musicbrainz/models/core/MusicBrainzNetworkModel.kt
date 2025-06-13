package ly.david.musicsearch.data.musicbrainz.models.core

import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.MusicBrainzModel

/**
 * Data that comes from MusicBrainz.
 */
interface MusicBrainzNetworkModel : MusicBrainzModel {
    val aliases: List<AliasMusicBrainzNetworkModel>?
}
