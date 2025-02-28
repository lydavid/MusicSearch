package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToLabelListItemModel
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import lydavidmusicsearchdatadatabase.Release_label

/**
 * This currently fulfills the role of both l_label_release (page releases by label)
 * and release_label (allows multiple catalog numbers for a release to be shown in release details screen).
 */
class ReleaseLabelDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.release_labelQueries

    @Suppress("SwallowedException")
    private fun insert(
        labelId: String,
        releaseId: String,
        catalogNumber: String,
    ): Int {
        return try {
            transacter.insertOrFail(
                Release_label(
                    release_id = releaseId,
                    label_id = labelId,
                    catalog_number = catalogNumber,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
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
    ): List<LabelListItemModel> = transacter.getLabelsByRelease(
        releaseId = releaseId,
        mapper = ::mapToLabelListItemModel,
    ).executeAsList()

    fun deleteLabelsByReleaseLinks(releaseId: String) {
        withTransaction {
            transacter.deleteLabelsByReleaseLinks(releaseId = releaseId)
        }
    }
    // endregion

    // region releases by label

    // TODO: we cannot use the same linking table for releases by label and labels by release
    //  since we page with an offset based on the count of links, by supporting both directions
    //  it is possible to never find all records
    fun linkReleasesByLabel(
        labelId: String,
        releases: List<ReleaseMusicBrainzModel>,
    ): Int {
        return transacter.transactionWithResult {
            releases.forEach { release ->
                release.labelInfoList?.forEach { labelInfo ->
                    insert(
                        releaseId = release.id,
                        labelId = labelId,
                        catalogNumber = labelInfo.catalogNumber.orEmpty(),
                    )
                }
            }
            releases.size
        }
    }

    fun observeCountOfReleasesByLabel(labelId: String): Flow<Int> =
        transacter.getNumberOfReleasesByLabel(
            labelId = labelId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfReleasesByLabel(labelId: String): Int =
        transacter.getNumberOfReleasesByLabel(
            labelId = labelId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getReleasesByLabel(
        labelId: String,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByLabel(
            labelId = labelId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByLabel(
                labelId = labelId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    fun deleteReleasesByLabel(labelId: String) {
        transacter.deleteReleasesByLabel(labelId = labelId)
    }
    // endregion
}
