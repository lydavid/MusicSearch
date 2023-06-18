package ly.david.data.room.artist

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.relation.RelationRoomModel

data class ArtistWithAllData(

    @Embedded
    val artist: ArtistRoomModel,

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
