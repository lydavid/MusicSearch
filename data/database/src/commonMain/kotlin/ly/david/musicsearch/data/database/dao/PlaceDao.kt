package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Place

class PlaceDao(
    database: Database,
) : EntityDao {
    override val transacter = database.placeQueries

    fun insert(place: PlaceMusicBrainzModel) {
        place.run {
            transacter.insert(
                Place(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    address = address,
                    type = type,
                    type_id = typeId,
                    longitude = coordinates?.longitude,
                    latitude = coordinates?.latitude,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                )
            )
        }
    }

    fun insertAll(places: List<PlaceMusicBrainzModel>) {
        transacter.transaction {
            places.forEach { place ->
                insert(place)
            }
        }
    }

    fun getPlace(placeId: String): Place? {
        return transacter.getPlace(placeId).executeAsOneOrNull()
    }
}
