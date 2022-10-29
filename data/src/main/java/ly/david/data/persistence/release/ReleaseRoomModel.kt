package ly.david.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ly.david.data.Release
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.getFormatsForDisplay
import ly.david.data.network.getTracksForDisplay
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel

@Entity(
    tableName = "releases",
    foreignKeys = [
        ForeignKey(
            entity = ReleaseGroupRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_group_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["release_group_id"], unique = false)]
)
data class ReleaseRoomModel(
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

    @ColumnInfo(name = "release_group_id", defaultValue = "")
    val releaseGroupId: String
) : RoomModel, Release

fun ReleaseMusicBrainzModel.toReleaseRoomModel() =
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
        tracks = media?.getTracksForDisplay(),
        releaseGroupId = releaseGroup?.id ?: throw ReleaseMissingReleaseGroupException()
    )

/**
 * We cannot make [ReleaseMusicBrainzModel.releaseGroup] non-null because we need to accommodate
 * [ReleaseMusicBrainzModel]. So we will throw if this ever happens.
 */
private class ReleaseMissingReleaseGroupException : Exception()
