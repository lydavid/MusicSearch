package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri

internal object MusicBrainzEntityStringColumnAdapter : ColumnAdapter<MusicBrainzEntity, String> {
    override fun decode(databaseValue: String): MusicBrainzEntity =
        MusicBrainzEntity.values().first { it.resourceUri == databaseValue }

    override fun encode(value: MusicBrainzEntity): String = value.resourceUri
}
