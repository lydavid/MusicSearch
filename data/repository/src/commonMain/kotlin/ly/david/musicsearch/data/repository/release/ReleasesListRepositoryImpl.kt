package ly.david.musicsearch.data.repository.release

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.musicbrainz.api.ALIASES
import ly.david.musicsearch.data.musicbrainz.api.ARTIST_CREDITS
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.LABELS
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.list.SortOption
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.release.ReleaseSortOption
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import kotlin.time.Instant

class ReleasesListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseApi: BrowseApi,
    private val releaseDao: ReleaseDao,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    aliasDao: AliasDao,
) : ReleasesListRepository,
    BrowseEntities<ReleaseListItemModel, ReleaseMusicBrainzNetworkModel, BrowseReleasesResponse>(
        browseEntity = MusicBrainzEntityType.RELEASE,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeReleases(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
        now: Instant,
    ): Flow<PagingData<ReleaseListItemModel>> {
        return listenBrainzAuthStore.browseUsername.flatMapLatest { username ->
            observeEntities(
                browseMethod = browseMethod,
                listFilters = listFilters.copy(
                    username = username,
                ),
                now = now,
            )
        }
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseListItemModel> {
        return releaseDao.getReleases(
            browseMethod = browseMethod,
            query = listFilters.query,
            username = listFilters.username,
            sortOption = (listFilters.sortOption as? SortOption.Release)?.option ?: ReleaseSortOption.InsertedAscending,
            showReleaseStatuses = (listFilters.sortOption as? SortOption.Release)?.showStatuses ?: emptySet(),
        )
    }

    override fun deleteEntityLinksByEntity(
        entity: MusicBrainzEntity,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entity.id,
                browseEntity = browseEntity,
            )

            when (entity.type) {
                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entity.id)
                }

                MusicBrainzEntityType.LABEL -> {
                    releaseDao.deleteReleasesByLabel(entity.id)
                }

                else -> releaseDao.deleteReleaseLinksByEntity(entity.id)
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleasesResponse {
        return when (entity.type) {
            MusicBrainzEntityType.LABEL -> {
                browseApi.browseReleasesByEntity(
                    entityId = entity.id,
                    entity = entity.type,
                    offset = offset,
                    include = "$ARTIST_CREDITS+$LABELS+$ALIASES",
                )
            }

            else -> {
                browseApi.browseReleasesByEntity(
                    entityId = entity.id,
                    entity = entity.type,
                    offset = offset,
                )
            }
        }
    }

    override fun insertAll(
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ReleaseMusicBrainzNetworkModel>,
    ) {
        releaseDao.insertAll(musicBrainzModels)
        when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entity.id,
                    entityIds = musicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntityType.LABEL -> {
                releaseDao.insertReleasesByLabel(
                    labelId = entity.id,
                    releases = musicBrainzModels,
                )
            }

            else -> releaseDao.insertReleasesByEntity(
                entityId = entity.id,
                releases = musicBrainzModels,
            )
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                releaseDao.getCountOfReleasesByCollectionQuery(
                    collectionId = entity.id,
                    query = "",
                    showReleaseStatuses = ReleaseStatus.entries.toSet(),
                ).executeAsOne().toInt()
            }

            MusicBrainzEntityType.LABEL -> {
                releaseDao.getCountOfReleasesByLabelQuery(
                    labelId = entity.id,
                    query = "",
                    showReleaseStatuses = ReleaseStatus.entries.toSet(),
                ).executeAsOne().toInt()
            }

            else -> releaseDao.getCountOfReleasesByEntityQuery(
                entityId = entity.id,
                query = "",
                showReleaseStatuses = ReleaseStatus.entries.toSet(),
            ).executeAsOne().toInt()
        }
    }
}
