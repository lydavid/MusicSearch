package ly.david.data.room.artist

import androidx.room.DatabaseView
import androidx.room.Embedded
import ly.david.data.room.relation.RelationRoomModel

@DatabaseView(
    value = """
        SELECT *
        FROM relation r
        WHERE r.linked_entity = "url"
    """,
    viewName = "url_relation"
)
data class UrlRelation(
    @Embedded
    val relation: RelationRoomModel,
)
