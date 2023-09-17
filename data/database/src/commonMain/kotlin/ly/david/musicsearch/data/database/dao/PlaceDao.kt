package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Mb_place

class PlaceDao(
    database: Database,
) {
    private val transacter = database.mb_placeQueries

    fun insert(place: PlaceMusicBrainzModel) {
        place.run {
            transacter.insert(
                Mb_place(
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

    fun getPlace(placeId: String): Mb_place? {
        return transacter.getPlace(placeId).executeAsOneOrNull()
    }
}
