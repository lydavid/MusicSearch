package ly.david.musicsearch.data.repository.release

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
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
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository

class ReleasesListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseApi: BrowseApi,
    private val releaseDao: ReleaseDao,
    aliasDao: AliasDao,
) : ReleasesListRepository,
    BrowseEntities<ReleaseListItemModel, ReleaseMusicBrainzNetworkModel, BrowseReleasesResponse>(
        browseEntity = MusicBrainzEntityType.RELEASE,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    override fun observeReleases(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ReleaseListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseListItemModel> {
        return releaseDao.getReleases(
            browseMethod = browseMethod,
            query = listFilters.query,
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
                MusicBrainzEntityType.AREA -> {
                    releaseDao.deleteReleasesByCountry(entityId)
                }

                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                MusicBrainzEntityType.LABEL -> {
                    releaseDao.deleteReleasesByLabel(entityId)
                }

                else -> releaseDao.deleteReleaseLinksByEntity(entityId)
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
        offset: Int,
    ): BrowseReleasesResponse {
        return when (entity) {
            MusicBrainzEntityType.LABEL -> {
                browseApi.browseReleasesByEntity(
                    entityId = entityId,
                    entity = entity,
                    offset = offset,
                    include = "$ARTIST_CREDITS+$LABELS+$ALIASES",
                )
            }

            else -> {
                browseApi.browseReleasesByEntity(
                    entityId = entityId,
                    entity = entity,
                    offset = offset,
                )
            }
        }
    }

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntityType,
        musicBrainzModels: List<ReleaseMusicBrainzNetworkModel>,
    ) {
        releaseDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntityType.AREA -> {
                releaseDao.insertReleasesByCountry(
                    areaId = entityId,
                    releases = musicBrainzModels,
                )
            }

            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntityType.LABEL -> {
                releaseDao.insertReleasesByLabel(
                    labelId = entityId,
                    releases = musicBrainzModels,
                )
            }

            else -> releaseDao.insertReleasesByEntity(
                entityId = entityId,
                releases = musicBrainzModels,
            )
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
    ): Int {
        return when (entity) {
            MusicBrainzEntityType.AREA -> {
                releaseDao.getCountOfReleasesByCountry(entityId)
            }

            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            MusicBrainzEntityType.LABEL -> {
                releaseDao.getCountOfReleasesByLabel(entityId)
            }

            else -> releaseDao.getCountOfReleasesByEntity(entityId)
        }
    }
}
