package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Release_group

class ReleaseGroupDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
) : EntityDao {
    override val transacter = database.release_groupQueries

    fun insert(releaseGroup: ReleaseGroupMusicBrainzModel) {
        releaseGroup.run {
            transacter.insert(
                Release_group(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    first_release_date = firstReleaseDate,
                    primary_type = primaryType,
                    primary_type_id = primaryTypeId,
                    secondary_types = secondaryTypes,
                    secondary_type_ids = secondaryTypeIds,
                )
            )
            artistCreditDao.insertArtistCredits(
                entityId = releaseGroup.id,
                artistCredits = artistCredits,
            )
        }
    }

    fun insertAll(releaseGroups: List<ReleaseGroupMusicBrainzModel>) {
        transacter.transaction {
            releaseGroups.forEach { releaseGroup ->
                insert(releaseGroup)
            }
        }
    }

    fun getReleaseGroup(releaseGroupId: String): Release_group? =
        transacter.getReleaseGroup(releaseGroupId).executeAsOneOrNull()
}
