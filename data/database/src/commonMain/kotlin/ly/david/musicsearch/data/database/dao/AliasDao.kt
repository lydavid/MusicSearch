package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzNetworkModel
import lydavidmusicsearchdatadatabase.AliasQueries

class AliasDao(
    database: Database,
) : EntityDao {
    override val transacter: AliasQueries = database.aliasQueries

    fun insertReplaceAll(
        musicBrainzNetworkModels: List<MusicBrainzNetworkModel>,
    ) {
        musicBrainzNetworkModels.forEach { musicBrainzNetworkModel ->
            transacter.delete(musicBrainzNetworkModel.id)
            musicBrainzNetworkModel.aliases?.forEach { alias ->
                insert(musicBrainzNetworkModel.id, alias)
            }
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
