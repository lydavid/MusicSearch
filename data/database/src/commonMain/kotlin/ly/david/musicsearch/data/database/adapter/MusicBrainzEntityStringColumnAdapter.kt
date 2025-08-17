package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.resourceUri

internal object MusicBrainzEntityStringColumnAdapter : ColumnAdapter<MusicBrainzEntityType, String> {
    override fun decode(databaseValue: String): MusicBrainzEntityType =
        MusicBrainzEntityType.entries.first { it.resourceUri == databaseValue }

    override fun encode(value: MusicBrainzEntityType): String = value.resourceUri
}
