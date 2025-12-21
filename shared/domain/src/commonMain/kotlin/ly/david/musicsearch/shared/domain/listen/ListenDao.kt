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

    /**
     * ([listenedAtMs], [username], [recordingMessyBrainzId]) are the minimal pieces of data needed to
     * uniquely identify a listen.
     *
     * [currentTimeMs] marks when a listen was deleted.
     */
    fun markListenForDeletion(
        listenedAtMs: Long,
        username: String,
        recordingMessyBrainzId: String,
        currentTimeMs: Long,
    )

    /**
     * Unmark all in case user marked multiple listens for deletion.
     */
    fun unmarkListenForDeletion()

    /**
     * Minimum pieces of data needed for deletion in ListenBrainz.
     */
    fun getListenTimestampAndMsidMarkedForDeletion(): List<Pair<Long, String>>

    fun deleteListens()
}
