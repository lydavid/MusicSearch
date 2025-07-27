package ly.david.musicsearch.data.repository.helpers

import kotlinx.coroutines.test.TestScope
import ly.david.data.test.api.NoOpCoverArtArchiveApi
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtUrls
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.data.repository.image.MusicBrainzImageMetadataRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface TestMusicBrainzImageMetadataRepository {

    val imageUrlDao: ImageUrlDao
    val coroutineDispatchers: CoroutineDispatchers

    fun createMusicBrainzImageMetadataRepository(
        coverArtUrlsProducer: (id: String, entity: MusicBrainzEntity) -> List<CoverArtUrls>,
    ): MusicBrainzImageMetadataRepository {
        return MusicBrainzImageMetadataRepositoryImpl(
            coverArtArchiveApi = object : NoOpCoverArtArchiveApi() {
                override suspend fun getCoverArts(
                    mbid: String,
                    entity: MusicBrainzEntity,
                ): CoverArtsResponse {
                    return CoverArtsResponse(
                        coverArtUrls = coverArtUrlsProducer(mbid, entity),
                    )
                }
            },
            imageUrlDao = imageUrlDao,
            logger = object : Logger {
                override fun d(text: String) {
                    println(text)
                }

                override fun e(exception: Exception) {
                    error(exception)
                }
            },
            coroutineScope = TestScope(coroutineDispatchers.io),
            coroutineDispatchers = coroutineDispatchers,
        )
    }
}