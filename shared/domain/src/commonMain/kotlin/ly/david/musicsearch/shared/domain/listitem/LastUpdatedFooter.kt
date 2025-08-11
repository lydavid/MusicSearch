package ly.david.musicsearch.shared.domain.listitem

import kotlin.time.Instant

data class LastUpdatedFooter(
    override val id: String = "LastUpdatedFooter",
    val lastUpdated: Instant,
) : ListItemModel
