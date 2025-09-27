package ly.david.musicsearch.shared.domain.listen

import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.list.FacetListItem
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface ListenDao {
    fun insert(
        listens: List<Listen>,
    )

    fun deleteListensByUser(username: String)

    fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?>

    fun getListensByUser(
        username: String,
        query: String,
        facetEntity: MusicBrainzEntity?,
    ): PagingSource<Int, ListenListItemModel>

    fun getFacetsByUser(
        entityType: MusicBrainzEntityType,
        username: String,
        query: String,
    ): PagingSource<Int, FacetListItem>

    fun getLatestTimestampMsByUser(username: String): Long?

    fun getOldestTimestampMsByUser(username: String): Long?

    fun updateMetadata(
        recordingMessyBrainzId: String,
        artistName: String?,
        entityMapping: Listen.EntityMapping,
    )
}
