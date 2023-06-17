package ly.david.data.room.artist

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import ly.david.data.Artist
import ly.david.data.LifeSpan
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.room.RoomModel
import ly.david.data.room.relation.RelationRoomModel

@Entity(
    tableName = "artist"
)
data class ArtistRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String = "",
    @ColumnInfo(name = "sort_name") override val sortName: String = "",
    @ColumnInfo(name = "disambiguation") override val disambiguation: String? = null,
    @ColumnInfo(name = "type") override val type: String? = null,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @ColumnInfo(name = "gender") override val gender: String? = null,
    @ColumnInfo(name = "country_code") override val countryCode: String? = null,
    @Embedded override val lifeSpan: LifeSpan? = null,
) : RoomModel, Artist

fun ArtistMusicBrainzModel.toArtistRoomModel() = ArtistRoomModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    typeId = typeId,
    gender = gender,
    countryCode = countryCode,
    lifeSpan = lifeSpan
)

data class ArtistWithAllData(

    @Embedded
    val artist: ArtistRoomModel,

    // TODO: link only urls. use a view or change query
    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val urls: List<UrlRelation>
)

@DatabaseView(
    value = """
        SELECT *
        FROM relation r
        WHERE r.linked_resource = "url"
    """,
    viewName = "url_relation"
)
data class UrlRelation(
    @Embedded
    val relation: RelationRoomModel
)
