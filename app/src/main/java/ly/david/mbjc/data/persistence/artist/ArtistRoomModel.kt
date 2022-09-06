package ly.david.mbjc.data.persistence.artist

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.ArtistMusicBrainzModel
import ly.david.mbjc.data.persistence.RoomModel

@Entity(
    tableName = "artists"
)
internal data class ArtistRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String = "",
    @ColumnInfo(name = "sort-name")
    override val sortName: String = "",
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String? = null,

    @ColumnInfo(name = "type")
    override val type: String? = null,
//    @ColumnInfo(name = "type-id")
//    val typeId: String? = null,

    @ColumnInfo(name = "gender")
    override val gender: String? = null,
//    @ColumnInfo(name = "gender-id")
//    val genderId: String? = null,

    @ColumnInfo(name = "country_code")
    override val countryCode: String? = null,

    // Allow nested fields to be part of this Room table. Good for data that doesn't require its own table.
    @Embedded
    override val lifeSpan: LifeSpan? = null,

    /**
     * The total number of release groups this artist has in Music Brainz's database.
     *
     * We track this number so that we know whether or not we've collected them all in our local database.
     *
     * When not set, it means we have not queried for the number of release groups by this artist.
     * Some artists may have 0 release groups, so 0 is considered set.
     */
    @ColumnInfo(name = "release_group_count")
    val releaseGroupsCount: Int? = null,

    /**
     * Flag to determine whether we should fetch their default relations.
     */
    @ColumnInfo(name = "has_default_relations", defaultValue = "false")
    val hasDefaultRelations: Boolean = false,

) : RoomModel, Artist

internal fun ArtistMusicBrainzModel.toArtistRoomModel(
    hasDefaultRelations: Boolean = false,
) =
    ArtistRoomModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan,
        hasDefaultRelations = hasDefaultRelations
    )
