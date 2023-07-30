package ly.david.data.domain.listitem

import ly.david.data.Identifiable
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
 * Data that can be displayed in lists should extend this.
 */
sealed class ListItemModel : Identifiable

/**
 * Content that appears in front of a list of [ListItemModel].
 * There should only be at most one header per list.
 *
 * @param isListEmpty Whether there's any list items besides this header.
 */
class Header(
    override val id: String = "Header",
    val isListEmpty: Boolean,
) : ListItemModel()

/**
 * Represents the end of the list of [ListItemModel] being displayed.
 */
data object EndOfList : ListItemModel() {
    override val id: String = "EndOfList"
}

/**
 * Represents a separator with [text] that can be inserted between two list items.
 */
class ListSeparator(
    override val id: String,
    val text: String,
) : ListItemModel()

/**
 * Converts a [MusicBrainzModel] that we got from the network to its UI version for display.
 *
 * We can map a [MusicBrainzModel] to [ListItemModel] but not the other way around
 * because there are [ListItemModel] such as [EndOfList] that do not have a 1-to-1 mapping.
 * We could still do it, but the result will be nullable.
 *
 * It seems like this needs to be in the same directory as [ListItemModel] or else it tells us to add an else branch.
 */
fun MusicBrainzModel.toListItemModel(): ListItemModel {
    return when (this) {
        is ArtistMusicBrainzModel -> this.toArtistListItemModel()
        is ReleaseGroupMusicBrainzModel -> this.toReleaseGroupListItemModel()
        is ReleaseMusicBrainzModel -> this.toReleaseListItemModel()
        is RecordingMusicBrainzModel -> this.toRecordingListItemModel()
        is PlaceMusicBrainzModel -> this.toPlaceListItemModel()
        is AreaMusicBrainzModel -> this.toAreaListItemModel()
        is InstrumentMusicBrainzModel -> this.toInstrumentListItemModel()
        is LabelMusicBrainzModel -> this.toLabelListItemModel()
        is WorkMusicBrainzModel -> this.toWorkListItemModel()
        is EventMusicBrainzModel -> this.toEventListItemModel()
        is SeriesMusicBrainzModel -> this.toSeriesListItemModel()
        is GenreMusicBrainzModel -> this.toGenreListItemModel()
        else -> error(
            "Converting collection MusicBrainz models directly to list item models not supported."
        )
    }
}
