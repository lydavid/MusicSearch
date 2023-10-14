package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.area.AreaScaffoldModel
import ly.david.musicsearch.data.core.listitem.EndOfList
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

    fun getArea(areaId: String): AreaScaffoldModel? {
        return transacter.getArea(
            id = areaId,
            mapper = ::toAreaScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun toAreaScaffoldModel(
        id: String,
        name: String,
        sort_name: String,
        disambiguation: String?,
        type: String?,
        type_id: String?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = AreaScaffoldModel(
        id = EndOfList.id,
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
