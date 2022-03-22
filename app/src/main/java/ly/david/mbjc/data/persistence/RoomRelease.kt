package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.MusicBrainzRelease
import ly.david.mbjc.data.network.getFormatsForDisplay
import ly.david.mbjc.data.network.getTracksForDisplay

@Entity(tableName = "releases")
data class RoomRelease(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,
    @ColumnInfo(name = "name")
    override val name: String,
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String,
    @ColumnInfo(name = "date")
    override val date: String?,
    @ColumnInfo(name = "status")
    override val status: String?,
    @ColumnInfo(name = "barcode")
    override val barcode: String?,
    @ColumnInfo(name = "status_id")
    override val statusId: String?,
    @ColumnInfo(name = "country")
    override val country: String?,
    @ColumnInfo(name = "packaging")
    override val packaging: String?,
    @ColumnInfo(name = "packaging_id")
    override val packagingId: String?,
    @ColumnInfo(name = "asin")
    override val asin: String?,
    @ColumnInfo(name = "quality")
    override val quality: String?,

    @ColumnInfo(name = "formats")
    val formats: String?,

    @ColumnInfo(name = "tracks")
    val tracks: String?
) : RoomData(), Release

fun MusicBrainzRelease.toRoomRelease() =
    RoomRelease(
        id = id,
        name = name,
        disambiguation = disambiguation,
        date = date,
        status = status,
        barcode = barcode,
        statusId = statusId,
        country = country,
        packaging = packaging,
        packagingId = packagingId,
        asin = asin,
        quality = quality,
        formats = media?.getFormatsForDisplay(),
        tracks = media?.getTracksForDisplay()
    )
