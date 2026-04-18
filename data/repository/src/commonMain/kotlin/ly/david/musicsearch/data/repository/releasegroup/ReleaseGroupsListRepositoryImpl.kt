package ly.david.musicsearch.data.repository.releasegroup

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertSeparators
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
import ly.david.musicsearch.shared.domain.list.SortOption
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListSeparator
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSortOption
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
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
                    val showTypeDividers = setOf(
                        ReleaseGroupSortOption.PrimaryTypeAscending,
                        ReleaseGroupSortOption.PrimaryTypeDescending,
                    ).contains((listFilters.sortOption as? SortOption.ReleaseGroup)?.option)
                    when {
                        showTypeDividers && rg2 != null &&
                            (rg1?.primaryType != rg2.primaryType || rg1?.secondaryTypes != rg2.secondaryTypes)
                        -> {
                            ReleaseGroupListSeparator(
                                id = "${rg1?.id}_${rg2.id}",
                                types = rg2,
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
            sortOption = (listFilters.sortOption as? SortOption.ReleaseGroup)?.option
                ?: ReleaseGroupSortOption.InsertedAscending,
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
                MusicBrainzEntityType.ARTIST -> {
                    releaseGroupDao.deleteReleaseGroupLinksByEntity(entity.id)
                }

                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entity.id)
                }

                else -> error(browseEntitiesNotSupported(entity.type))
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleaseGroupsResponse {
        return browseApi.browseReleaseGroupsByEntity(
            entityId = entity.id,
            entity = entity.type,
            offset = offset,
        )
    }

    override fun insertAll(
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ReleaseGroupMusicBrainzNetworkModel>,
    ) {
        releaseGroupDao.insertAllReleaseGroups(musicBrainzModels)
        when (entity.type) {
            MusicBrainzEntityType.ARTIST -> {
                releaseGroupDao.insertReleaseGroupsByEntity(
                    entityId = entity.id,
                    releaseGroupIds = musicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entity.id,
                    entityIds = musicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            else -> error(browseEntitiesNotSupported(entity.type))
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entity.id)
            }

            else -> {
                releaseGroupDao.getCountOfReleaseGroupsByArtist(entity.id)
            }
        }
    }
}
