package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypes

data class ReleaseGroupListSeparator(
    override val id: String,
    val types: ReleaseGroupTypes,
) : ListItemModel
