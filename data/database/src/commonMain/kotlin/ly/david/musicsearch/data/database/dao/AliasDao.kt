package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import lydavidmusicsearchdatadatabase.AliasQueries

class AliasDao(
    database: Database,
) : EntityDao {
    override val transacter: AliasQueries = database.aliasQueries

    fun insertReplaceAll(
        mbid: String,
        aliases: List<AliasMusicBrainzNetworkModel>,
    ) {
        transacter.delete(mbid)
        aliases.forEach { alias ->
            insert(mbid, alias)
        }
    }

    private fun insert(
        mbid: String,
        alias: AliasMusicBrainzNetworkModel?,
    ) {
        alias?.run {
            transacter.insert(
                id = 0,
                mbid = mbid,
                name = name,
                locale = locale.orEmpty(),
                typeId = typeId,
                isPrimary = isPrimary == true,
                beginDate = beginDate.orEmpty(),
                endDate = endDate.orEmpty(),
            )
        }
    }
}
