package ly.david.musicsearch.data.musicbrainz.models.core

import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.MusicBrainzModel

/**
 * Data that comes from MusicBrainz.
 */
interface MusicBrainzNetworkModel : MusicBrainzModel {
    val aliases: List<AliasMusicBrainzNetworkModel>?
    val relations: List<RelationMusicBrainzModel>?
    val genres: List<GenreMusicBrainzNetworkModel>?
    val tags: List<TagMusicBrainzNetworkModel>?
    val userGenres: List<GenreMusicBrainzNetworkModel>?
    val userTags: List<TagMusicBrainzNetworkModel>?
}
