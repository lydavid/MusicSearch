package ly.david.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Release
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.TextRepresentation
import ly.david.data.network.getFormatsForDisplay
import ly.david.data.network.getTracksForDisplay
import ly.david.data.persistence.RoomModel

@Entity(
    tableName = "releases",
)
data class ReleaseRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,
    @ColumnInfo(name = "name")
    override val name: String,
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String = "",

    // Date could be 2022-10-10 or 2022 (or possibly 2022-10)
    @ColumnInfo(name = "date")
    override val date: String? = null,

    @ColumnInfo(name = "barcode")
    override val barcode: String? = null,
    @ColumnInfo(name = "asin")
    override val asin: String? = null,
    @ColumnInfo(name = "quality")
    override val quality: String? = null,

    @ColumnInfo(name = "country_code")
    override val countryCode: String? = null,

    // TODO: separate table
    @ColumnInfo(name = "status")
    override val status: String? = null,
    @ColumnInfo(name = "status_id")
    override val statusId: String? = null,

    // TODO: separate table
    @ColumnInfo(name = "packaging")
    override val packaging: String? = null,
    @ColumnInfo(name = "packaging_id")
    override val packagingId: String? = null,

    @Embedded
    override val coverArtArchive: CoverArtArchive = CoverArtArchive(),

    @Embedded
    override val textRepresentation: TextRepresentation? = null,

    @ColumnInfo(name = "formats")
    val formats: String? = null,

    @ColumnInfo(name = "tracks")
    val tracks: String? = null,

    /**
     * May be one of:
     * - `null`: Have not requested cover art
     * - Empty: Requested but did not find any
     * - string url: URL to cover art
     */
    @ColumnInfo(name = "cover_art_url", defaultValue = "null")
    val coverArtUrl: String? = null,

    @ColumnInfo(name = "release_group_id", defaultValue = "null")
    val releaseGroupId: String? = null
) : RoomModel, Release

fun ReleaseMusicBrainzModel.toRoomModel(releaseGroupId: String?) =
    ReleaseRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        date = date,
        status = status,
        barcode = barcode,
        statusId = statusId,
        countryCode = countryCode,
        packaging = packaging,
        packagingId = packagingId,
        asin = asin,
        quality = quality,
        coverArtArchive = coverArtArchive,
        textRepresentation = textRepresentation,
        formats = media?.getFormatsForDisplay(),
        tracks = media?.getTracksForDisplay(),
        releaseGroupId = releaseGroupId
    )

fun ReleaseMusicBrainzModel.toRoomModel() =
    this.toRoomModel(releaseGroup?.id)
