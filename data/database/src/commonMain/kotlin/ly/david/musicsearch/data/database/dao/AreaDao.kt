package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.AreaMusicBrainzModel
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
                )
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

    fun getArea(areaId: String): Area? {
        return transacter.getArea(areaId).executeAsOneOrNull()
    }
}
