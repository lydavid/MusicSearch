package ly.david.data.room.area.releases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.room.release.ReleaseRoomModel

// MB has a country_area table between this and area.
// Right now we don't have a use for that.
/**
 * This records a single release event for a release.
 * Release events seems to always be in a country.
 *
 * Note: for area, only countries have a list of releases.
 * Other area type may have relationships with releases, but they won't be found via browse.
 */
@Entity(
    // releases_countries suggests this is many-to-many between releases and countries (areas)
    // however we currently only have release as a primary key
    // In order to also have area as a primary key, we need to store all areas from a release's release events
    tableName = "release_country",
    primaryKeys = ["release_id", "country_id"],
    foreignKeys = [
        ForeignKey(
            entity = ReleaseRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
// TODO: test inserting these from area releases tab first
//  then test inserting these from release details tab
data class ReleaseCountry(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "country_id")
    val countryId: String,

    // Date could be 2022-10-10 or 2022 (or possibly 2022-10)
    @ColumnInfo(name = "date")
    val date: String? = null,
)

fun ReleaseMusicBrainzModel.getReleaseCountries(): List<ReleaseCountry> =
    releaseEvents?.mapNotNull { releaseEvent ->
        val countryId = releaseEvent.area?.id
        if (countryId == null) {
            null
        } else {
            ReleaseCountry(
                releaseId = id,
                countryId = countryId,
                date = releaseEvent.date
            )
        }
    }.orEmpty()
