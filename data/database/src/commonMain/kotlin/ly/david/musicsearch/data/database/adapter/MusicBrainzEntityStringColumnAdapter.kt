package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri

internal object MusicBrainzEntityStringColumnAdapter : ColumnAdapter<MusicBrainzEntity, String> {
    override fun decode(databaseValue: String): MusicBrainzEntity =
        MusicBrainzEntity.entries.first { it.resourceUri == databaseValue }

    override fun encode(value: MusicBrainzEntity): String = value.resourceUri
}
