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
import ly.david.musicsearch.shared.domain.common.toUUID
import ly.david.musicsearch.shared.domain.error.Action
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.list.FacetListItem
import ly.david.musicsearch.shared.domain.listen.Listen
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListensListFeedback
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import kotlin.uuid.ExperimentalUuidApi

class ListensListRepositoryImpl(
    private val listenDao: ListenDao,
    private val listenBrainzApi: ListenBrainzApi,
    private val coroutineScope: CoroutineScope,
) : ListensListRepository {
    override fun observeListens(
        username: String,
        query: String,
        entityFacet: MusicBrainzEntity?,
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
                facetEntity = entityFacet,
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
        facetEntity: MusicBrainzEntity?,
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
                .takeIf { facetEntity == null },
            pagingSourceFactory = {
                listenDao.getListensByUser(
                    username = username,
                    query = query,
                    facetEntity = facetEntity,
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

    override fun observeFacets(
        entityType: MusicBrainzEntityType,
        username: String,
        query: String,
    ): Flow<PagingData<FacetListItem>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            pagingSourceFactory = {
                listenDao.getFacetsByUser(
                    entityType = entityType,
                    username = username,
                    query = query,
                )
            },
        ).flow
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun submitManualMapping(
        recordingMessyBrainzId: String,
        rawRecordingMusicBrainzId: String,
    ): Feedback<ListensListFeedback> {
        return try {
            val recordingId = rawRecordingMusicBrainzId.toUUID()
            listenBrainzApi.submitManualMapping(
                recordingMessyBrainzId = recordingMessyBrainzId,
                recordingMusicBrainzId = recordingId.toHexDashString(),
            )

            refreshMapping(recordingMessyBrainzId = recordingMessyBrainzId)
        } catch (ex: IllegalArgumentException) {
            Feedback.Error(
                data = ListensListFeedback.InvalidID(ex.message),
                errorResolution = ErrorResolution.None,
            )
        } catch (ex: HandledException) {
            Feedback.Error(
                data = when {
                    ex.errorResolution == ErrorResolution.Login ->
                        ListensListFeedback.NeedToLogin

                    else -> ex.message?.let { ListensListFeedback.NetworkException(it) }
                        ?: ListensListFeedback.FailToSubmitMapping
                },
                errorResolution = ErrorResolution.None,
            )
        }
    }

    override suspend fun refreshMapping(
        recordingMessyBrainzId: String,
    ): Feedback<ListensListFeedback> {
        return try {
            val manualMappingResponse = listenBrainzApi.getManualMapping(recordingMessyBrainzId)
            val recordingId = manualMappingResponse.mapping.recordingMbid
            val recordingMetadata = listenBrainzApi.getRecordingMetadata(recordingMusicBrainzId = recordingId)

            // ListenBrainz's submit endpoint accepts any UUID, but its recording metadata endpoint will
            // return an empty object if there was no recording with that UUID.
            // This let us warn the user that they passed a UUID that does not match any recordings.
            if (recordingMetadata?.recording?.name == null) {
                return Feedback.Error(
                    data = ListensListFeedback.NoRecording(
                        id = recordingId,
                    ),
                    errorResolution = ErrorResolution.None,
                )
            }

            listenDao.updateMetadata(
                recordingMessyBrainzId = recordingMessyBrainzId,
                artistName = recordingMetadata.artistCredit.name,
                entityMapping = Listen.EntityMapping(
                    recordingMusicbrainzId = recordingId,
                    recordingName = recordingMetadata.recording.name,
                    durationMs = recordingMetadata.recording.length,
                    caaId = recordingMetadata.release?.caaId,
                    caaReleaseMbid = recordingMetadata.release?.caaReleaseMbid,
                    releaseMbid = recordingMetadata.release?.mbid,
                    releaseName = recordingMetadata.release?.name,
                    artistCredits = recordingMetadata.artistCredit.artists.map { artist ->
                        ArtistCreditUiModel(
                            artistId = artist.artistMbid,
                            name = artist.name,
                            joinPhrase = artist.joinPhrase,
                        )
                    },
                ),
            )
            Feedback.Success(
                data = ListensListFeedback.Updated,
            )
        } catch (ex: HandledException) {
            when {
                ex.errorResolution == ErrorResolution.Login -> {
                    Feedback.Error(
                        data = ListensListFeedback.NeedToLogin,
                        errorResolution = ErrorResolution.None,
                    )
                }

                ex.errorType == ErrorType.NotFound -> {
                    Feedback.Error(
                        data = ListensListFeedback.NoManualMapping,
                        errorResolution = ErrorResolution.None,
                    )
                }

                else -> {
                    Feedback.Error(
                        data = ex.message?.let { ListensListFeedback.NetworkException(it) }
                            ?: ListensListFeedback.FailToRefreshMapping,
                        errorResolution = ErrorResolution.None,
                    )
                }
            }
        }
    }

    override fun markListenForDeletion(
        listenedAtMs: Long,
        username: String,
        recordingMessyBrainzId: String,
        currentTimeMs: Long,
    ): ActionableResult {
        listenDao.markListenForDeletion(
            listenedAtMs = listenedAtMs,
            username = username,
            recordingMessyBrainzId = recordingMessyBrainzId,
            currentTimeMs = currentTimeMs,
        )
        return ActionableResult(
            message = "Deleting...",
            action = Action.Undo,
        )
    }

    override fun unmarkListenForDeletion() {
        listenDao.unmarkListenForDeletion()
    }

    override suspend fun deleteMarkedForDeletion(): Feedback<ListensListFeedback> {
        return try {
            val listensTimestampAndMsidMarkedForDeletion = listenDao.getListenTimestampAndMsidMarkedForDeletion()
            listensTimestampAndMsidMarkedForDeletion.forEach { listensTimestampAndMsid ->
                listenBrainzApi.deleteListen(
                    listenedAtMs = listensTimestampAndMsid.first,
                    recordingMessyBrainzId = listensTimestampAndMsid.second,
                )
            }
            listenDao.deleteListens()

            Feedback.Success(
                data = ListensListFeedback.DeletedNumberOfListens(
                    size = listensTimestampAndMsidMarkedForDeletion.size,
                ),
            )
        } catch (ex: HandledException) {
            Feedback.Error(
                data = when {
                    ex.errorResolution == ErrorResolution.Login ->
                        ListensListFeedback.NeedToLogin

                    else -> ex.message?.let { ListensListFeedback.NetworkException(it) }
                        ?: ListensListFeedback.FailToDeleteListen
                },
                errorResolution = ErrorResolution.None,
            )
        }
    }
}
