package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Release

class ReleaseDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val mediumDao: MediumDao,
) : EntityDao {
    override val transacter = database.releaseQueries

    fun insertAll(releases: List<ReleaseMusicBrainzModel>) {
        transacter.transaction {
            releases.forEach { release ->
                insert(release)
            }
        }
    }

    fun insert(release: ReleaseMusicBrainzModel) {
        release.run {
            transacter.insert(
                Release(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    date = date,
                    status = status,
                    barcode = barcode,
                    status_id = statusId,
                    country_code = countryCode,
                    packaging = packaging,
                    packaging_id = packagingId,
                    asin = asin,
                    quality = quality,
                    cover_art_count = coverArtArchive.count,
                    script = textRepresentation?.script,
                    language = textRepresentation?.language,
                )
            )
            artistCreditDao.insertArtistCredits(
                entityId = id,
                artistCredits = artistCredits,
            )
            mediumDao.insertAll(
                releaseId = id,
                media = media,
            )
        }
    }

    fun getRelease(releaseId: String): Release? =
        transacter.getRelease(releaseId).executeAsOneOrNull()
}
