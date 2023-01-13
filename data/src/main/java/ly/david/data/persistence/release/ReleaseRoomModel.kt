package ly.david.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Release
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.TextRepresentation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel

@Entity(
    tableName = "release",
)
data class ReleaseRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String = "",

    /**
     * Valid date formats are 2022-10-10, 2022-10, or 2022
     *
     * Although this information is mostly duplicated from release_country, it is used
     * in the case where no countries are associated with its release.
     */
    @ColumnInfo(name = "date") override val date: String? = null,

    @ColumnInfo(name = "barcode") override val barcode: String? = null,
    @ColumnInfo(name = "asin") override val asin: String? = null,
    @ColumnInfo(name = "quality") override val quality: String? = null,

    @ColumnInfo(name = "country_code") override val countryCode: String? = null,

    // TODO: separate table
    @ColumnInfo(name = "status") override val status: String? = null,
    @ColumnInfo(name = "status_id") override val statusId: String? = null,

    // TODO: separate table
    @ColumnInfo(name = "packaging") override val packaging: String? = null,
    @ColumnInfo(name = "packaging_id") override val packagingId: String? = null,

    @Embedded override val coverArtArchive: CoverArtArchive = CoverArtArchive(),

    @Embedded override val textRepresentation: TextRepresentation? = null,

    /**
     * We do not have to store the release id since we can build it based on [id].
     *
     * May be one of:
     * - `null`: Have not requested cover art
     * - Empty: Requested but did not find any
     * - string path: partial url path to cover art (eg. 32187188956-250.jpg)
     *
     * Also see [ReleaseGroupRoomModel.coverArtPath].
     */
    @ColumnInfo(name = "cover_art_path", defaultValue = "null") val coverArtPath: String? = null,
) : RoomModel, Release

fun ReleaseMusicBrainzModel.toRoomModel() =
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
        textRepresentation = textRepresentation
    )
