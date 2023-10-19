package ly.david.musicsearch.data.musicbrainz

import ly.david.musicsearch.core.models.Identifiable
import ly.david.musicsearch.core.models.NameWithDisambiguation

/**
 * Data that comes from MusicBrainz.
 */
sealed class MusicBrainzModel : Identifiable, NameWithDisambiguation
