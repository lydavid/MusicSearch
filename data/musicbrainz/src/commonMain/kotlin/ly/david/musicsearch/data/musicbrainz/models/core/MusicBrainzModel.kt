package ly.david.musicsearch.data.musicbrainz.models.core

import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.NameWithDisambiguation

/**
 * Data that comes from MusicBrainz.
 */
sealed class MusicBrainzModel : Identifiable, NameWithDisambiguation
