package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupScaffoldModel
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

    fun getReleaseGroupForDetails(releaseGroupId: String): ReleaseGroupScaffoldModel? =
        transacter.getReleaseGroupForDetails(
            releaseGroupId = releaseGroupId,
            mapper = ::mapToReleaseGroupForDetails,
        ).executeAsOneOrNull()

    private fun mapToReleaseGroupForDetails(
        id: String,
        name: String,
        firstReleaseDate: String,
        disambiguation: String,
        primaryType: String?,
        secondaryTypes: List<String>?,
        thumbnailUrl: String?,
    ) = ReleaseGroupScaffoldModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        imageUrl = thumbnailUrl,
    )

    fun getReleaseGroupForRelease(releaseId: String): ReleaseGroupForRelease? =
        transacter.getReleaseGroupForRelease(
            releaseId = releaseId,
            mapper = { id, name, firstReleaseDate, disambiguation, primaryType, _, secondaryTypes, _ ->
                ReleaseGroupForRelease(
                    id = id,
                    name = name,
                    firstReleaseDate = firstReleaseDate,
                    disambiguation = disambiguation,
                    primaryType = primaryType,
                    secondaryTypes = secondaryTypes
                )
            }
        ).executeAsOneOrNull()
}
