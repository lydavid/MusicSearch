package ly.david.mbjc.data.domain

import ly.david.mbjc.data.network.ArtistMusicBrainzModel
import ly.david.mbjc.data.network.MusicBrainzModel
import ly.david.mbjc.data.network.PlaceMusicBrainzModel
import ly.david.mbjc.data.network.RecordingMusicBrainzModel
import ly.david.mbjc.data.network.ReleaseGroupMusicBrainzModel
import ly.david.mbjc.data.network.ReleaseMusicBrainzModel

/**
 * Data that can be displayed in our app should extend this.
 */
internal sealed class UiModel

/**
 * Represents the end of the list of [UiModel] being displayed.
 */
internal object EndOfList : UiModel()

/**
 * Represents a separator with [text] that can be inserted between two list items.
 */
internal class ListSeparator(val text: String) : UiModel()

/**
 * Converts a [MusicBrainzModel] that we got from the network to its UI version for display.
 *
 * We can map a [MusicBrainzModel] to [UiModel] but not the other way around because there are [UiModel] such as
 * [EndOfList] that do not have a 1-to-t mapping. We could still do it, but the result will be nullable.
 *
 * It seems like this needs to be in the same directory as [UiModel] or else it tells us to add an else branch.
 */
internal fun MusicBrainzModel.toUiModel(): UiModel {
    return when (this) {
        is ArtistMusicBrainzModel -> this.toArtistUiModel()
        is ReleaseGroupMusicBrainzModel -> this.toReleaseGroupUiModel()
        is ReleaseMusicBrainzModel -> this.toReleaseUiModel()
        is RecordingMusicBrainzModel -> this.toRecordingUiModel()
        is PlaceMusicBrainzModel -> this.toPlaceUiModel()
    }
}
