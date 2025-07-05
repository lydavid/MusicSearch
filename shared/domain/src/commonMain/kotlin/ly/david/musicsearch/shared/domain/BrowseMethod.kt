package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

// TODO: combine all classes that uses this, and switch on it based on MusicBrainzEntity
//  All should accept MusicBrainzEntity as well
//  Everytime I change one of these, I have to change them everywhere, so they belong together
//  Yes, there will be 10+ parameters injected, just gotta accept it
sealed class BrowseMethod {
    data object All : BrowseMethod()
    data class ByEntity(
        val entityId: String,
        val entity: MusicBrainzEntity,
    ) : BrowseMethod()
}
