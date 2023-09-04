package ly.david.data.room.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Release
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.room.RoomModel

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

    // TODO: might be able to remove this unless we care about the number of cover art it has
    @Embedded override val coverArtArchive: CoverArtArchiveRoomModel = CoverArtArchiveRoomModel(),

    @Embedded override val textRepresentation: TextRepresentationRoomModel? = null,
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
        coverArtArchive = coverArtArchive.toCoverArtArchiveRoomModel(),
        textRepresentation = textRepresentation?.toTextRepresentationRoomModel(),
    )
