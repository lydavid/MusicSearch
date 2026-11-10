package ly.david.musicsearch.shared.domain.tag.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.listitem.TaggedEntityListItemModel
import ly.david.musicsearch.shared.domain.tag.TagRepository

class GetEntitiesByTagOrGenre(
    private val tagRepository: TagRepository,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        name: String,
        query: String,
    ): Flow<PagingData<TaggedEntityListItemModel>> =
        tagRepository.observeEntitiesByTagOrGenre(
            name = name,
        )
            .distinctUntilChanged()
            .cachedIn(coroutineScope)
}
