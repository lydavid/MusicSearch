package ly.david.musicsearch.data.repository.releasegroup

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import app.cash.paging.TerminalSeparatorType
import app.cash.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import kotlin.time.Instant

class ReleaseGroupsListRepositoryImpl(
    private val collectionEntityDao: CollectionEntityDao,
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val browseApi: BrowseApi,
    private val releaseGroupDao: ReleaseGroupDao,
    aliasDao: AliasDao,
) : ReleaseGroupsListRepository,
    BrowseEntities<ReleaseGroupListItemModel, ReleaseGroupMusicBrainzNetworkModel, BrowseReleaseGroupsResponse>(
        browseEntity = MusicBrainzEntityType.RELEASE_GROUP,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    override fun observeReleaseGroups(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
        now: Instant,
    ): Flow<PagingData<ListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
            now = now,
        ).map { pagingData ->
            pagingData
                .insertSeparators(
                    terminalSeparatorType = TerminalSeparatorType.SOURCE_COMPLETE,
                ) { rg1: ReleaseGroupListItemModel?, rg2: ReleaseGroupListItemModel? ->
                    when {
                        listFilters.sorted && rg2 != null &&
                            (rg1?.primaryType != rg2.primaryType || rg1.secondaryTypes != rg2.secondaryTypes)
                        -> {
                            ListSeparator(
                                id = "${rg1?.id}_${rg2.id}",
                                text = rg2.getDisplayTypes(),
                            )
                        }

                        else -> {
                            null
                        }
                    }
                }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseGroupListItemModel> {
        return releaseGroupDao.getReleaseGroups(
            browseMethod = browseMethod,
            query = listFilters.query,
            sorted = listFilters.sorted,
        )
    }

    override fun deleteEntityLinksByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntityType.ARTIST -> {
                    releaseGroupDao.deleteReleaseGroupLinksByEntity(entityId)
                }

                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
        offset: Int,
    ): BrowseReleaseGroupsResponse {
        return browseApi.browseReleaseGroupsByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntityType,
        musicBrainzModels: List<ReleaseGroupMusicBrainzNetworkModel>,
    ) {
        releaseGroupDao.insertAllReleaseGroups(musicBrainzModels)
        when (entity) {
            MusicBrainzEntityType.ARTIST -> {
                releaseGroupDao.insertReleaseGroupsByEntity(
                    entityId = entityId,
                    releaseGroupIds = musicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
    ): Int {
        return when (entity) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            else -> {
                releaseGroupDao.getCountOfReleaseGroupsByArtist(entityId)
            }
        }
    }
}
