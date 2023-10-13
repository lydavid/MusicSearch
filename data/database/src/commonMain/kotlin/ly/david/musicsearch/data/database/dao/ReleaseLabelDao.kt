package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.label.LabelWithCatalog
import ly.david.musicsearch.data.core.release.ReleaseForListItem
import ly.david.data.musicbrainz.LabelInfo
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseForListItem
import lydavidmusicsearchdatadatabase.Release_label

/**
 * This currently fulfills the role of both l_label_release (page releases by release)
 * and release_label (allows multiple catalog numbers for a release to be shown in release details screen).
 */
class ReleaseLabelDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.release_labelQueries

    private fun insert(
        labelId: String,
        releaseId: String,
        catalogNumber: String,
    ) {
        transacter.insert(
            Release_label(
                release_id = releaseId,
                label_id = labelId,
                catalog_number = catalogNumber,
            )
        )
    }

    // region labels by release
    fun linkLabelsByRelease(
        releaseId: String,
        labelInfoList: List<LabelInfo>?,
    ) {
        transacter.transaction {
            labelInfoList?.forEach { labelInfo ->
                val labelId = labelInfo.label?.id ?: return@forEach
                insert(
                    releaseId = releaseId,
                    labelId = labelId,
                    catalogNumber = labelInfo.catalogNumber.orEmpty(),
                )
            }
        }
    }

    fun getLabelsByRelease(
        releaseId: String,
    ): List<LabelWithCatalog> = transacter.getLabelsByRelease(
        releaseId = releaseId,
        mapper = ::mapToLabelWithCatalog,
    ).executeAsList()

    private fun mapToLabelWithCatalog(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        labelCode: Int?,
        catalogNumber: String,
    ) = LabelWithCatalog(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
        catalogNumber = catalogNumber
    )
    // endregion

    fun getNumberOfReleasesByLabel(labelId: String): Flow<Int> =
        transacter.getNumberOfReleasesByLabel(
            labelId = labelId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    // region releases by label
    fun linkReleasesByLabel(
        labelId: String,
        releases: List<ReleaseMusicBrainzModel>,
    ) {
        transacter.transaction {
            releases.forEach { release ->
                release.labelInfoList?.forEach { labelInfo ->
                    insert(
                        releaseId = release.id,
                        labelId = labelId,
                        catalogNumber = labelInfo.catalogNumber.orEmpty(),
                    )
                }
            }
        }
    }

    fun getReleasesByLabel(
        labelId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByLabel(
            labelId = labelId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getReleasesByLabel(
            labelId = labelId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseForListItem,
        )
    }

    fun deleteReleasesByLabel(labelId: String) {
        withTransaction {
            transacter.deleteReleasesByLabel(labelId)
            transacter.deleteLabelReleaseLinks(labelId)
        }
    }
    // endregion
}
