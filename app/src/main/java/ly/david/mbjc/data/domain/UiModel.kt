package ly.david.mbjc.data.domain

import ly.david.mbjc.data.network.MusicBrainzArtist
import ly.david.mbjc.data.network.MusicBrainzData
import ly.david.mbjc.data.network.MusicBrainzRelease
import ly.david.mbjc.data.network.MusicBrainzReleaseGroup

/**
 * Data that can be displayed in our app should extend this.
 */
sealed class UiModel

/**
 * Represents the end of the list of [UiModel] being displayed.
 */
object EndOfList : UiModel()

/**
 * Represents a separator with [text] that can be inserted between two list items.
 */
class ListSeparator(val text: String) : UiModel()

/**
 * Converts a [MusicBrainzData] that we got from the network to its UI version for display.
 *
 * We can map a [MusicBrainzData] to [UiModel] but not the other way around because there are [UiModel] such as
 * [EndOfList] that do not have a 1-to-t mapping. We could still do it, but the result will be nullable.
 *
 * It seems like this needs to be in the same directory as [UiModel] or else it tells us to add an else branch.
 */
fun MusicBrainzData.toUiModel(): UiModel {
    return when (this) {
        is MusicBrainzArtist -> this.toArtistUiModel()
        is MusicBrainzReleaseGroup -> this.toReleaseGroupUiModel()
        is MusicBrainzRelease -> this.toReleaseUiModel()
    }
}
