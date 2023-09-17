package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Mb_area

class AreaDao(
    database: Database,
) {
    private val transacter = database.mb_areaQueries

    fun insert(area: AreaMusicBrainzModel) {
        area.run {
            transacter.insert(
                Mb_area(
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

    fun getArea(areaId: String): Mb_area? {
        return transacter.getArea(areaId).executeAsOneOrNull()
    }
}
