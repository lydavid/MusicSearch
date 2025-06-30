package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

sealed interface EntityListItemModel : ListItemModel, NameWithDisambiguation, Collectible, Visitable
