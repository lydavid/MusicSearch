package ly.david.musicsearch.data.repository.release

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertSeparators
import androidx.paging.map
import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.database.dao.TrackAndMedium
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.shared.domain.area.AreaType
import ly.david.musicsearch.shared.domain.area.NonCountryAreaWithCode
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.shared.domain.listitem.toAreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
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
    private val tagDao: TagDao,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listenBrainzRepository: ListenBrainzRepository,
    private val lookupApi: LookupApi,
    private val appPreferences: AppPreferences,
    detailsMetadataDao: DetailsMetadataDao,
    coroutineDispatchers: CoroutineDispatchers,
    musicBrainzAuthStore: MusicBrainzAuthStore,
) : ReleaseRepository, LookupEntityRepository<ReleaseDetailsModel, ReleaseMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    detailsMetadataDao = detailsMetadataDao,
    coroutineDispatchers = coroutineDispatchers,
    musicBrainzAuthStore = musicBrainzAuthStore,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        releaseDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): ReleaseDetailsModel? {
        if (!visited(entityId)) return null
        val releaseGroup = releaseGroupDao.getReleaseGroupForRelease(entityId) ?: return null

        val username = listenBrainzAuthStore.browseUsername.first()
        val numberOfListensToShow = appPreferences.observeNumberOfListensInDetails.first()
        val release = releaseDao.getReleaseForDetails(
            releaseId = entityId,
            listenBrainzUsername = username,
            numberOfListensToShow = numberOfListensToShow,
        ) ?: return null

        val artistCredits = artistCreditDao.getArtistCreditsForEntity(entityId)
        val labels = labelDao.getLabelsByRelease(entityId)
        val releaseEvents = areaDao.getCountriesByRelease(entityId)
        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.RELEASE,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
        // releases must have artist credits and a release group.
        return release.copy(
            artistCredits = artistCredits.toPersistentList(),
            releaseGroup = releaseGroup,
            labels = labels.toPersistentList(),
            areas = releaseEvents.map { it.toAreaListItemModel() }.toPersistentList(),
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
            listenBrainzUrl = "${listenBrainzRepository.getBaseUrl()}/album/${releaseGroup.id}",
        )
    }

    override suspend fun getRemoteData(
        entityId: String,
        include: String,
    ): ReleaseMusicBrainzNetworkModel {
        return lookupApi.lookupRelease(
            releaseId = entityId,
            include = include +
                "+artist-credits" +
                "+labels" +
                "+recordings" + // gives us tracks
                "+release-groups" + // gives us types
                "+isrcs",
        )
    }

    override fun delete(entityId: String) {
        super.delete(entityId)

        releaseDao.delete(releaseId = entityId)
        releaseReleaseGroupDao.deleteReleaseGroupByReleaseLink(releaseId = entityId)
        labelDao.deleteReleaseLabelLinks(releaseId = entityId)
        areaDao.deleteCountriesByReleaseLinks(releaseId = entityId)
        artistCreditDao.deleteArtistCreditsForEntity(entityId = entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: ReleaseMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        musicBrainzNetworkModel.releaseGroup?.let { releaseGroup ->
            releaseGroupDao.upsertReleaseGroup(
                oldId = releaseGroup.id,
                releaseGroup = releaseGroup,
            )
            releaseReleaseGroupDao.insert(
                releaseId = musicBrainzNetworkModel.id,
                releaseGroupId = releaseGroup.id,
            )
        }
        releaseDao.upsert(
            oldId = oldId,
            release = musicBrainzNetworkModel,
        )

        // This serves as a replacement for browsing labels by release.
        // Unless we find a release that has more than 25 labels, we don't need to browse for labels.
        // Lifespan is not included, so do not upsert.
        labelDao.insertAll(musicBrainzNetworkModel.labelInfoList?.mapNotNull { it.label })
        labelDao.insertLabelsByRelease(
            releaseId = musicBrainzNetworkModel.id,
            labelInfoList = musicBrainzNetworkModel.labelInfoList,
        )

        areaDao.insertAll(
            musicBrainzNetworkModel.releaseEvents?.mapNotNull { event ->
                event.area?.let { area ->
                    val isCountry = !NonCountryAreaWithCode.entries.any {
                        it.code == area.countryCodes?.firstOrNull().orEmpty()
                    }

                    area.copy(
                        type = AreaType.Country.name.takeIf { isCountry },
                        typeId = AreaType.Country.id.takeIf { isCountry },
                    )
                }
            }.orEmpty(),
        )
        areaDao.linkCountriesByRelease(
            releaseId = musicBrainzNetworkModel.id,
            releaseEvents = musicBrainzNetworkModel.releaseEvents,
        )

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
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
                    lookupEntity(
                        entityId = releaseId,
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
        artists = artistCredits,
        visited = visited,
        collected = collected,
        mediumPosition = mediumPosition,
        mediumName = mediumName,
        trackCount = trackCount,
        format = format,
        aliases = aliases,
        listenCount = listenCount,
    )
