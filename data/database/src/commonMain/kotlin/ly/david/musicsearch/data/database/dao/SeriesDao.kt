package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Series
import lydavidmusicsearchdatadatabase.SeriesQueries

class SeriesDao(
    database: Database,
) : EntityDao {
    override val transacter: SeriesQueries = database.seriesQueries

    fun insert(series: SeriesMusicBrainzModel) {
        series.run {
            transacter.insert(
                Series(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                ),
            )
        }
    }

    fun insertAll(series: List<SeriesMusicBrainzModel>) {
        transacter.transaction {
            series.forEach { series ->
                insert(series)
            }
        }
    }

    fun getSeriesForDetails(seriesId: String): SeriesDetailsModel? {
        return transacter.getSeries(
            seriesId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
    ) = SeriesDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )

    fun delete(id: String) {
        transacter.delete(id)
    }
}
