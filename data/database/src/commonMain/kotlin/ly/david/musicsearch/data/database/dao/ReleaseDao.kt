package ly.david.musicsearch.data.database.dao

import ly.david.data.core.release.FormatTrackCount
import ly.david.data.core.release.ReleaseForDetails
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

    fun delete(releaseId: String) {
        transacter.deleteRelease(releaseId)
    }

    fun getReleaseForDetails(releaseId: String): ReleaseForDetails? =
        transacter.getReleaseForDetails(
            releaseId = releaseId,
            mapper = ::mapToReleaseForDetails,
        ).executeAsOneOrNull()

    private fun mapToReleaseForDetails(
        id: String,
        name: String,
        disambiguation: String,
        date: String?,
        barcode: String?,
        asin: String?,
        quality: String?,
        countryCode: String?,
        status: String?,
        statusId: String?,
        packaging: String?,
        packagingId: String?,
        script: String?,
        language: String?,
        coverArtCount: Int,
        imageUrl: String?,
        releaseLength: Double?,
        hasNullLength: Boolean,
    ) = ReleaseForDetails(
        id = id,
        name = name,
        disambiguation = disambiguation,
        date = date,
        barcode = barcode,
        asin = asin,
        quality = quality,
        countryCode = countryCode,
        status = status,
        statusId = statusId,
        packaging = packaging,
        packagingId = packagingId,
        script = script,
        language = language,
        coverArtCount = coverArtCount,
        imageUrl = imageUrl,
        releaseLength = releaseLength?.toInt(),
        hasNullLength = hasNullLength,
    )

    fun getReleaseFormatTrackCount(releaseId: String): List<FormatTrackCount> =
        transacter.getReleaseFormatTrackCount(
            releaseId = releaseId,
            mapper = { format, trackCount ->
                FormatTrackCount(
                    format = format,
                    trackCount = trackCount.toInt(),
                )
            }
        ).executeAsList()
}
