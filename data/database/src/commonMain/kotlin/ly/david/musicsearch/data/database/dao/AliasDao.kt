package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import lydavidmusicsearchdatadatabase.AliasQueries

class AliasDao(
    database: Database,
) : EntityDao {
    override val transacter: AliasQueries = database.aliasQueries

    fun insertReplaceAll(
        musicBrainzNetworkModels: List<MusicBrainzNetworkModel>,
    ) {
        musicBrainzNetworkModels.forEach { musicBrainzNetworkModel ->
            delete(
                networkModel = musicBrainzNetworkModel,
            )
            musicBrainzNetworkModel.aliases?.forEach { alias ->
                insert(
                    networkModel = musicBrainzNetworkModel,
                    alias = alias,
                )
            }
        }
    }

    private fun delete(
        networkModel: MusicBrainzNetworkModel,
    ) {
        when (networkModel) {
            is AreaMusicBrainzNetworkModel -> transacter.deleteAreaAlias(networkModel.id)
            is ArtistMusicBrainzNetworkModel -> transacter.deleteArtistAlias(networkModel.id)
            is EventMusicBrainzNetworkModel -> transacter.deleteEventAlias(networkModel.id)
            is GenreMusicBrainzNetworkModel -> transacter.deleteGenreAlias(networkModel.id)
            is InstrumentMusicBrainzNetworkModel -> transacter.deleteInstrumentAlias(networkModel.id)
            is LabelMusicBrainzNetworkModel -> transacter.deleteLabelAlias(networkModel.id)
            is PlaceMusicBrainzNetworkModel -> transacter.deletePlaceAlias(networkModel.id)
            is RecordingMusicBrainzNetworkModel -> transacter.deleteRecordingAlias(networkModel.id)
            is ReleaseMusicBrainzNetworkModel -> transacter.deleteReleaseAlias(networkModel.id)
            is ReleaseGroupMusicBrainzNetworkModel -> transacter.deleteReleaseGroupAlias(networkModel.id)
            is SeriesMusicBrainzNetworkModel -> transacter.deleteSeriesAlias(networkModel.id)
            is WorkMusicBrainzNetworkModel -> transacter.deleteWorkAlias(networkModel.id)
            else -> error("Cannot delete alias for unsupported entity: ${networkModel::class.simpleName})")
        }
    }

    @Suppress("LongMethod")
    private fun insert(
        networkModel: MusicBrainzNetworkModel,
        alias: AliasMusicBrainzNetworkModel?,
    ) {
        alias?.copy(
            locale = alias.locale?.replace("_", "-"),
        )?.run {
            when (networkModel) {
                is AreaMusicBrainzNetworkModel -> {
                    transacter.insertAreaAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is ArtistMusicBrainzNetworkModel -> {
                    transacter.insertArtistAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is EventMusicBrainzNetworkModel -> {
                    transacter.insertEventAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is GenreMusicBrainzNetworkModel -> {
                    transacter.insertGenreAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is InstrumentMusicBrainzNetworkModel -> {
                    transacter.insertInstrumentAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is LabelMusicBrainzNetworkModel -> {
                    transacter.insertLabelAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is PlaceMusicBrainzNetworkModel -> {
                    transacter.insertPlaceAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is RecordingMusicBrainzNetworkModel -> {
                    transacter.insertRecordingAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is ReleaseMusicBrainzNetworkModel -> {
                    transacter.insertReleaseAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is ReleaseGroupMusicBrainzNetworkModel -> {
                    transacter.insertReleaseGroupAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is SeriesMusicBrainzNetworkModel -> {
                    transacter.insertSeriesAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                is WorkMusicBrainzNetworkModel -> {
                    transacter.insertWorkAlias(
                        id = 0,
                        mbid = networkModel.id,
                        name = name,
                        locale = locale.orEmpty(),
                        typeId = typeId,
                        isPrimary = isPrimary == true,
                        beginDate = beginDate.orEmpty(),
                        endDate = endDate.orEmpty(),
                        ended = ended == true,
                    )
                }

                else -> error("Cannot insert alias for unsupported entity: ${networkModel::class.simpleName}")
            }
        }
    }

    fun getAliases(
        entityType: MusicBrainzEntity,
        mbid: String,
    ): List<BasicAlias> {
        val aliasQueries: Map<MusicBrainzEntity, AliasQuery<BasicAlias>> = mapOf(
            MusicBrainzEntity.AREA to transacter::getAreaAliases,
            MusicBrainzEntity.ARTIST to transacter::getArtistAliases,
            MusicBrainzEntity.EVENT to transacter::getEventAliases,
            MusicBrainzEntity.GENRE to transacter::getGenreAliases,
            MusicBrainzEntity.INSTRUMENT to transacter::getInstrumentAliases,
            MusicBrainzEntity.LABEL to transacter::getLabelAliases,
            MusicBrainzEntity.PLACE to transacter::getPlaceAliases,
            MusicBrainzEntity.RECORDING to transacter::getRecordingAliases,
            MusicBrainzEntity.RELEASE to transacter::getReleaseAliases,
            MusicBrainzEntity.RELEASE_GROUP to transacter::getReleaseGroupAliases,
            MusicBrainzEntity.SERIES to transacter::getSeriesAliases,
            MusicBrainzEntity.WORK to transacter::getWorkAliases,
        )

        return aliasQueries[entityType]?.invoke(mbid, mapToBasicAlias())?.executeAsList()
            ?: emptyList()
    }
}

private typealias AliasQuery<T> = (String, (String, String?, Boolean) -> T) -> Query<T>

private fun mapToBasicAlias(): (String, String?, Boolean) -> BasicAlias = { name, locale, isPrimary ->
    BasicAlias(
        name = name,
        locale = locale.orEmpty(),
        isPrimary = isPrimary,
    )
}
