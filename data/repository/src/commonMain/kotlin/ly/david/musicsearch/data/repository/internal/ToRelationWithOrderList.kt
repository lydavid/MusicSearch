package ly.david.musicsearch.data.repository.internal

import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.relation.RelationWithOrder
import ly.david.musicsearch.data.database.dao.toRelationDatabaseModel

internal fun List<RelationMusicBrainzModel>?.toRelationWithOrderList(
    entityId: String,
): List<RelationWithOrder> =
    this?.mapIndexedNotNull { index, relationMusicBrainzModel ->
        relationMusicBrainzModel.toRelationDatabaseModel(
            entityId = entityId,
            index = index,
            numberOfRelationships = this.size,
        )
    }.orEmpty()
