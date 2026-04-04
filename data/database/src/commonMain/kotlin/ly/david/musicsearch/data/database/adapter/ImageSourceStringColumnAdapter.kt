package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import ly.david.musicsearch.shared.domain.image.ImageSource

internal object ImageSourceStringColumnAdapter : ColumnAdapter<ImageSource, String> {
    override fun decode(databaseValue: String): ImageSource = ImageSource.valueOf(databaseValue.uppercase())

    override fun encode(value: ImageSource): String = value.name.lowercase()
}
