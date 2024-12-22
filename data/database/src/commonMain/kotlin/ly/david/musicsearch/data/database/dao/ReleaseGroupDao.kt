package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import lydavidmusicsearchdatadatabase.Release_group

interface ReleaseGroupDao : EntityDao {
    fun insert(releaseGroup: ReleaseGroupMusicBrainzModel)
    fun insertAll(releaseGroups: List<ReleaseGroupMusicBrainzModel>)
    fun getReleaseGroupForDetails(releaseGroupId: String): ReleaseGroupDetailsModel?
    fun getReleaseGroupForRelease(releaseId: String): ReleaseGroupForRelease?
    fun delete(id: String)
}

class ReleaseGroupDaoImpl(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
) : ReleaseGroupDao {
    override val transacter = database.release_groupQueries

    override fun insert(releaseGroup: ReleaseGroupMusicBrainzModel) {
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
                ),
            )
            artistCreditDao.insertArtistCredits(
                entityId = releaseGroup.id,
                artistCredits = artistCredits,
            )
        }
    }

    override fun insertAll(releaseGroups: List<ReleaseGroupMusicBrainzModel>) {
        transacter.transaction {
            releaseGroups.forEach { releaseGroup ->
                insert(releaseGroup)
            }
        }
    }

    override fun getReleaseGroupForDetails(releaseGroupId: String): ReleaseGroupDetailsModel? =
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
        placeholderKey: Long?,
    ) = ReleaseGroupDetailsModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        imageUrl = thumbnailUrl,
        placeholderKey = placeholderKey,
    )

    override fun getReleaseGroupForRelease(releaseId: String): ReleaseGroupForRelease? =
        transacter.getReleaseGroupForRelease(
            releaseId = releaseId,
            mapper = { id, name, firstReleaseDate, disambiguation, primaryType, _, secondaryTypes, _ ->
                ReleaseGroupForRelease(
                    id = id,
                    name = name,
                    firstReleaseDate = firstReleaseDate,
                    disambiguation = disambiguation,
                    primaryType = primaryType,
                    secondaryTypes = secondaryTypes,
                )
            },
        ).executeAsOneOrNull()

    override fun delete(id: String) {
        transacter.delete(id)
    }
}
