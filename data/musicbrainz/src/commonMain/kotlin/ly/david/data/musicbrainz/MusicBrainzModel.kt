package ly.david.data.musicbrainz

import ly.david.musicsearch.data.core.Identifiable
import ly.david.musicsearch.data.core.NameWithDisambiguation

/**
 * Data that comes from MusicBrainz.
 */
sealed class MusicBrainzModel : Identifiable, NameWithDisambiguation
