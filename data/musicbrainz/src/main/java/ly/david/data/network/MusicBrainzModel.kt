package ly.david.data.network

import ly.david.data.MusicBrainzId
import ly.david.data.NameWithDisambiguation

// TODO: Identifiable?
/**
 * Data that comes from network call to Music Brainz.
 */
sealed class MusicBrainzModel : MusicBrainzId, NameWithDisambiguation
