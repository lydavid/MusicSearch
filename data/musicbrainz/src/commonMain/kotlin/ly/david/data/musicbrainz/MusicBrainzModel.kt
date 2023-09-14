package ly.david.data.musicbrainz

import ly.david.data.core.Identifiable
import ly.david.data.core.NameWithDisambiguation

/**
 * Data that comes from MusicBrainz.
 */
sealed class MusicBrainzModel : Identifiable, NameWithDisambiguation
