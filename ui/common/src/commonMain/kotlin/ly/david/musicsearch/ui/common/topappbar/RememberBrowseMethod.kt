package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

val BrowseMethodSaver: Saver<MutableState<BrowseMethod?>, Any> = run {
    val id = "id"
    val entity = "entity"
    val entityId = "entityId"
    mapSaver(
        save = {
            when (val browseMethod = it.value) {
                is BrowseMethod.All -> mapOf(id to BrowseMethod.All.toString())
                is BrowseMethod.ByEntity -> mapOf(
                    id to BrowseMethod.ByEntity::class.toString(),
                    entity to browseMethod.entity,
                    entityId to browseMethod.entityId,
                )
                null -> mapOf(id to "null")
            }
        },
        restore = {
            mutableStateOf(
                when (it[id]) {
                    BrowseMethod.All.toString() -> BrowseMethod.All
                    BrowseMethod.ByEntity::class.toString() -> BrowseMethod.ByEntity(
                        entityId = it[entityId] as String,
                        entity = it[entity] as MusicBrainzEntityType,
                    )

                    else -> null
                },
            )
        },
    )
}
