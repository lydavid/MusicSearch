package ly.david.data.domain

import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.GenreMusicBrainzModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.MusicBrainzModel
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.WorkMusicBrainzModel

/**
 * Data that can be displayed in our app should extend this.
 */
sealed class UiModel

/**
 * Content that appears in front of a list of [UiModel].
 */
object Header : UiModel()

/**
 * Represents the end of the list of [UiModel] being displayed.
 */
object EndOfList : UiModel()

/**
 * Represents a separator with [text] that can be inserted between two list items.
 */
class ListSeparator(val text: String) : UiModel()

/**
 * Converts a [MusicBrainzModel] that we got from the network to its UI version for display.
 *
 * We can map a [MusicBrainzModel] to [UiModel] but not the other way around because there are [UiModel] such as
 * [EndOfList] that do not have a 1-to-1 mapping. We could still do it, but the result will be nullable.
 *
 * It seems like this needs to be in the same directory as [UiModel] or else it tells us to add an else branch.
 */
fun MusicBrainzModel.toUiModel(): UiModel {
    return when (this) {
        is ArtistMusicBrainzModel -> this.toArtistUiModel()
        is ReleaseGroupMusicBrainzModel -> this.toReleaseGroupUiModel()
        is ReleaseMusicBrainzModel -> this.toReleaseUiModel()
        is RecordingMusicBrainzModel -> this.toRecordingUiModel()
        is PlaceMusicBrainzModel -> this.toPlaceUiModel()
        is AreaMusicBrainzModel -> this.toAreaUiModel()
        is InstrumentMusicBrainzModel -> this.toInstrumentUiModel()
        is LabelMusicBrainzModel -> this.toLabelUiModel()
        is WorkMusicBrainzModel -> this.toWorkUiModel()
        is EventMusicBrainzModel -> this.toEventUiModel()
        is SeriesMusicBrainzModel -> this.toSeriesUiModel()
        is GenreMusicBrainzModel -> this.toInstrumentUiModel()
    }
}
