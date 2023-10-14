package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.musicsearch.data.core.series.SeriesScaffoldModel
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
                )
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

    fun getSeriesForDetails(seriesId: String): SeriesScaffoldModel? {
        return transacter.getSeries(
            seriesId,
            mapper = ::toSeriesScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun toSeriesScaffoldModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
    ) = SeriesScaffoldModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )
}
