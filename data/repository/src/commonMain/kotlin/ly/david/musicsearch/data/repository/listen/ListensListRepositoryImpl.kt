package ly.david.musicsearch.data.repository.listen

import androidx.paging.ExperimentalPagingApi
import androidx.paging.TerminalSeparatorType
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import app.cash.paging.Pager
import app.cash.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.common.getDateFormatted
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listen.Listen
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.recording.RecordingFacet

class ListensListRepositoryImpl(
    private val listenDao: ListenDao,
    private val listenBrainzApi: ListenBrainzApi,
    private val coroutineScope: CoroutineScope,
) : ListensListRepository {
    override fun observeListens(
        username: String,
        query: String,
        recordingId: String?,
        stopPrepending: Boolean,
        stopAppending: Boolean,
        onReachedLatest: (Boolean) -> Unit,
        onReachedOldest: (Boolean) -> Unit,
    ): Flow<PagingData<Identifiable>> {
        return if (username.isEmpty()) {
            emptyFlow()
        } else {
            pagerFlow(
                username = username,
                query = query,
                recordingId = recordingId,
                stopPrepending = stopPrepending,
                stopAppending = stopAppending,
                onReachedLatest = onReachedLatest,
                onReachedOldest = onReachedOldest,
            )
        }
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun pagerFlow(
        username: String,
        query: String,
        recordingId: String?,
        stopPrepending: Boolean,
        stopAppending: Boolean,
        onReachedLatest: (Boolean) -> Unit,
        onReachedOldest: (Boolean) -> Unit,
    ): Flow<PagingData<Identifiable>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = ListenRemoteMediator(
                username = username,
                listenDao = listenDao,
                listenBrainzApi = listenBrainzApi,
                reachedLatest = stopPrepending,
                reachedOldest = stopAppending,
                onReachedLatest = onReachedLatest,
                onReachedOldest = onReachedOldest,
            )
                // Provide a smoother experience if we don't try to load data while the user is faceting on a recording
                .takeIf { recordingId == null },
            pagingSourceFactory = {
                listenDao.getListensByUser(
                    username = username,
                    query = query,
                    recordingId = recordingId,
                )
            },
        ).flow.map { pagingData ->
            pagingData.insertSeparators(terminalSeparatorType = TerminalSeparatorType.SOURCE_COMPLETE) {
                    before: ListenListItemModel?,
                    after: ListenListItemModel?,
                ->
                getListSeparator(
                    before = before,
                    after = after,
                )
            }
        }
    }

    private fun getListSeparator(
        before: ListenListItemModel?,
        after: ListenListItemModel?,
    ): ListSeparator? {
        if (after == null) return null

        val beforeDate = before?.listenedAt?.getDateFormatted()
        val afterDate = after.listenedAt.getDateFormatted()

        if (beforeDate == afterDate) return null

        return ListSeparator(
            id = after.listenedAtMs.toString(),
            text = afterDate,
        )
    }

    override fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?> {
        return listenDao.observeUnfilteredCountOfListensByUser(username = username)
    }

    override fun observeRecordingFacets(
        username: String,
        query: String,
    ): Flow<PagingData<RecordingFacet>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            pagingSourceFactory = {
                listenDao.getRecordingFacetsByUser(
                    username = username,
                    query = query,
                )
            },
        ).flow
    }

    override suspend fun refreshMapping(
        recordingMessyBrainzId: String,
    ): ActionableResult {
        return try {
            val manualMappingResponse = listenBrainzApi.getManualMapping(recordingMessyBrainzId)
            val recordingId = manualMappingResponse.mapping.recordingMbid
            val recordingMetadata = listenBrainzApi.getRecordingMetadata(recordingMusicBrainzId = recordingId)
            listenDao.updateMetadata(
                recordingMessyBrainzId = recordingMessyBrainzId,
                artistName = recordingMetadata?.artistCredit?.name,
                entityMapping = Listen.EntityMapping(
                    recordingMusicbrainzId = recordingId,
                    recordingName = recordingMetadata?.recording?.name,
                    durationMs = recordingMetadata?.recording?.length,
                    caaId = recordingMetadata?.release?.caaId,
                    caaReleaseMbid = recordingMetadata?.release?.caaReleaseMbid,
                    releaseMbid = recordingMetadata?.release?.mbid,
                    releaseName = recordingMetadata?.release?.name,
                    artistCredits = recordingMetadata?.artistCredit?.artists?.map { artist ->
                        ArtistCreditUiModel(
                            artistId = artist.artistMbid,
                            name = artist.name,
                            joinPhrase = artist.joinPhrase,
                        )
                    }.orEmpty(),
                ),
            )
            ActionableResult(
                message = "Updated",
            )
        } catch (ex: HandledException) {
            when {
                ex.errorResolution == ErrorResolution.Login -> {
                    ActionableResult(
                        message = "You need to be logged in to update this listen's mapping",
                    )
                }

                ex.errorType == ErrorType.NotFound -> {
                    ActionableResult(
                        message = "No manual mapping found for this listen",
                    )
                }

                else -> {
                    ActionableResult(
                        message = ex.message ?: "Unknown error",
                    )
                }
            }
        }
    }
}
