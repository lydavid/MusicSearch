package ly.david.musicsearch.data.repository.release

import androidx.paging.Pager
import androidx.paging.TerminalSeparatorType
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.PagingData
import app.cash.paging.insertSeparators
import app.cash.paging.map
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackAndMedium
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.area.AreaType
import ly.david.musicsearch.shared.domain.area.NonCountryAreaWithCode
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.shared.domain.listitem.toAreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import kotlin.time.Instant

class ReleaseRepositoryImpl(
    private val releaseDao: ReleaseDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val areaDao: AreaDao,
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val aliasDao: AliasDao,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listenBrainzRepository: ListenBrainzRepository,
    private val lookupApi: LookupApi,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ReleaseRepository {

    override suspend fun lookupRelease(
        releaseId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ReleaseDetailsModel = withContext(coroutineDispatchers.io) {
        val cachedData = getCachedData(releaseId)
        return@withContext if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val releaseMusicBrainzModel = lookupApi.lookupRelease(releaseId)
            releaseDao.withTransaction {
                if (forceRefresh) {
                    delete(releaseId)
                }
                cache(
                    oldId = releaseId,
                    release = releaseMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(releaseMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private suspend fun getCachedData(releaseId: String): ReleaseDetailsModel? {
        if (!relationRepository.visited(releaseId)) return null
        val releaseGroup = releaseGroupDao.getReleaseGroupForRelease(releaseId) ?: return null

        val username = listenBrainzAuthStore.browseUsername.first()
        val release = releaseDao.getReleaseForDetails(
            releaseId = releaseId,
            listenBrainzUsername = username,
        ) ?: return null

        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseId)
        val labels = labelDao.getLabelsByRelease(releaseId)
        val releaseEvents = areaDao.getCountriesByRelease(releaseId)
        val urlRelations = relationRepository.getRelationshipsByType(releaseId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.RELEASE,
            mbid = releaseId,
        )

        // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
        // releases must have artist credits and a release group.
        return release.copy(
            artistCredits = artistCredits.toPersistentList(),
            releaseGroup = releaseGroup,
            labels = labels,
            areas = releaseEvents.map { it.toAreaListItemModel() },
            urls = urlRelations,
            aliases = aliases,
            listenBrainzUrl = "${listenBrainzRepository.getBaseUrl()}/album/${releaseGroup.id}",
        )
    }

    private fun delete(releaseId: String) {
        releaseDao.withTransaction {
            releaseDao.delete(releaseId = releaseId)
            releaseReleaseGroupDao.deleteReleaseGroupByReleaseLink(releaseId = releaseId)
            labelDao.deleteReleaseLabelLinks(releaseId = releaseId)
            areaDao.deleteCountriesByReleaseLinks(releaseId = releaseId)
            relationRepository.deleteRelationshipsByType(entityId = releaseId)
            artistCreditDao.deleteArtistCreditsForEntity(entityId = releaseId)
        }
    }

    private fun cache(
        oldId: String,
        release: ReleaseMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        release.releaseGroup?.let { releaseGroup ->
            releaseGroupDao.upsertReleaseGroup(
                oldId = releaseGroup.id,
                releaseGroup = releaseGroup,
            )
            releaseReleaseGroupDao.insert(
                releaseId = release.id,
                releaseGroupId = releaseGroup.id,
            )
        }
        releaseDao.upsert(
            oldId = oldId,
            release = release,
        )

        aliasDao.insertAll(listOf(release))

        // This serves as a replacement for browsing labels by release.
        // Unless we find a release that has more than 25 labels, we don't need to browse for labels.
        // Lifespan is not included, so do not upsert.
        labelDao.insertAll(release.labelInfoList?.mapNotNull { it.label })
        labelDao.insertLabelsByRelease(
            releaseId = release.id,
            labelInfoList = release.labelInfoList,
        )

        areaDao.insertAll(
            release.releaseEvents?.mapNotNull { event ->
                event.area?.let { area ->
                    val isCountry = !NonCountryAreaWithCode.entries.any {
                        it.code == area.countryCodes?.firstOrNull().orEmpty()
                    }

                    area.copy(
                        type = if (isCountry) AreaType.COUNTRY else null,
                    )
                }
            }.orEmpty(),
        )
        areaDao.linkCountriesByRelease(
            releaseId = release.id,
            releaseEvents = release.releaseEvents,
        )

        val relationWithOrderList = release.relations.toRelationWithOrderList(release.id)
        relationRepository.insertRelations(
            entityId = release.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )
    }

    // region tracks by release
    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override fun observeTracksByRelease(
        releaseId: String,
        query: String,
        lastUpdated: Instant,
    ): Flow<PagingData<ListItemModel>> {
        return listenBrainzAuthStore.browseUsername.flatMapLatest { username ->
            getPagingFlow(
                releaseId = releaseId,
                lastUpdated = lastUpdated,
                query = query,
                username = username,
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getPagingFlow(
        releaseId: String,
        lastUpdated: Instant,
        query: String,
        username: String,
    ): Flow<PagingData<ListItemModel>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = LookupEntityRemoteMediator(
                // The initial release lookup will store all tracks.
                hasEntityBeenStored = { true },
                lookupEntity = {
                    lookupRelease(
                        releaseId = releaseId,
                        forceRefresh = false,
                        lastUpdated = lastUpdated,
                    )
                },
                deleteLocalEntity = { deleteMediaAndTracksByRelease(releaseId) },
            ),
            pagingSourceFactory = {
                trackDao.getTracksByRelease(
                    releaseId = releaseId,
                    query = query,
                    username = username,
                )
            },
        ).flow
            .map { pagingData ->
                pagingData
                    .map { it.toTrackListItemModel() }
                    .insertSeparators(
                        terminalSeparatorType = TerminalSeparatorType.SOURCE_COMPLETE,
                    ) { before: TrackListItemModel?, after: TrackListItemModel? ->
                        if (before?.mediumId != after?.mediumId && after != null) {
                            ListSeparator(
                                id = "${after.mediumId}",
                                text = "${after.mediumPosition}・" +
                                    after.format.orEmpty() +
                                    after.mediumName.transformThisIfNotNullOrEmpty { " ($it)" },
                            )
                        } else {
                            null
                        }
                    }
            }
    }

    private fun deleteMediaAndTracksByRelease(releaseId: String) {
        releaseDao.withTransaction {
            mediumDao.deleteMediaByRelease(releaseId)
            releaseDao.delete(releaseId)
        }
    }
    // endregion
}

private fun TrackAndMedium.toTrackListItemModel() =
    TrackListItemModel(
        id = id,
        position = position,
        number = number,
        name = title,
        length = length,
        mediumId = mediumId,
        recordingId = recordingId,
        formattedArtistCredits = formattedArtistCredits,
        visited = visited,
        mediumPosition = mediumPosition,
        mediumName = mediumName,
        trackCount = trackCount,
        format = format,
        aliases = aliases,
        listenCount = listenCount,
    )
