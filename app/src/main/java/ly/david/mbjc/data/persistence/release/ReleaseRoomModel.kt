package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.CoverArtArchive
import ly.david.mbjc.data.network.ReleaseMusicBrainzModel
import ly.david.mbjc.data.network.getFormatsForDisplay
import ly.david.mbjc.data.network.getTracksForDisplay
import ly.david.mbjc.data.persistence.RoomModel

@Entity(tableName = "releases")
internal data class ReleaseRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,
    @ColumnInfo(name = "name")
    override val name: String,
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String,

    // Date could be 2022-10-10 or 2022 (or possibly 2022-10)
    @ColumnInfo(name = "date")
    override val date: String?,

    @ColumnInfo(name = "barcode")
    override val barcode: String?,
    @ColumnInfo(name = "asin")
    override val asin: String?,
    @ColumnInfo(name = "quality")
    override val quality: String?,

    @ColumnInfo(name = "country_code")
    override val countryCode: String?,

    // TODO: separate table
    @ColumnInfo(name = "status")
    override val status: String?,
    @ColumnInfo(name = "status_id")
    override val statusId: String?,

    // TODO: separate table
    @ColumnInfo(name = "packaging")
    override val packaging: String?,
    @ColumnInfo(name = "packaging_id")
    override val packagingId: String?,

    @Embedded
    override val coverArtArchive: CoverArtArchive,

    @ColumnInfo(name = "formats")
    val formats: String?,

    @ColumnInfo(name = "tracks")
    val tracks: String?,

    /**
     * May be one of:
     * - `null`: Have not requested cover art
     * - Empty: Requested but did not find any
     * - string url: URL to cover art
     */
    @ColumnInfo(name = "cover_art_url", defaultValue = "null")
    val coverArtUrl: String? = null,
) : RoomModel, Release

internal fun ReleaseMusicBrainzModel.toReleaseRoomModel() =
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
        formats = media?.getFormatsForDisplay(),
        tracks = media?.getTracksForDisplay()
    )
