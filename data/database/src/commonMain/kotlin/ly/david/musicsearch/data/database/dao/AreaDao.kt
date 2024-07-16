package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.area.AreaDetailsModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.AreaQueries

class AreaDao(
    database: Database,
) : EntityDao {
    override val transacter: AreaQueries = database.areaQueries

    fun insert(area: AreaMusicBrainzModel) {
        area.run {
            transacter.insert(
                Area(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
        }
    }

    fun insertAll(areas: List<AreaMusicBrainzModel>) {
        transacter.transaction {
            areas.forEach { area ->
                insert(area)
            }
        }
    }

    fun getAreaForDetails(areaId: String): AreaDetailsModel? {
        return transacter.getArea(
            id = areaId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = AreaDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )
}
