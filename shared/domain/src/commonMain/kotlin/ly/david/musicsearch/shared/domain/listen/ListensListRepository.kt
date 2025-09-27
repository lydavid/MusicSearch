package ly.david.musicsearch.shared.domain.listen

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.list.FacetListItem

interface ListensListRepository {
    fun observeListens(
        username: String,
        query: String,
        entityFacet: MusicBrainzEntity?,
        stopPrepending: Boolean,
        stopAppending: Boolean,
        onReachedLatest: (Boolean) -> Unit,
        onReachedOldest: (Boolean) -> Unit,
    ): Flow<PagingData<Identifiable>>

    fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?>

    fun observeFacets(
        entityType: MusicBrainzEntityType,
        username: String,
        query: String,
    ): Flow<PagingData<FacetListItem>>

    suspend fun submitManualMapping(
        recordingMessyBrainzId: String,
        rawRecordingMusicBrainzId: String,
    ): ActionableResult

    suspend fun refreshMapping(
        recordingMessyBrainzId: String,
    ): ActionableResult
}
