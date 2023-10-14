package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.place.CoordinatesUiModel
import ly.david.musicsearch.data.core.place.PlaceScaffoldModel
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

    fun getPlaceForDetails(placeId: String): PlaceScaffoldModel? {
        return transacter.getPlace(
            placeId,
            mapper = ::mapToPlaceScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun mapToPlaceScaffoldModel(
        id: String,
        name: String,
        disambiguation: String?,
        address: String,
        type: String?,
        longitude: Double?,
        latitude: Double?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = PlaceScaffoldModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = CoordinatesUiModel(
            longitude = longitude,
            latitude = latitude,
        ),
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )
}
