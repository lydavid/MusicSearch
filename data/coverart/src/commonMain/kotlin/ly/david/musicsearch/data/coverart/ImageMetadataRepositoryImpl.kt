package ly.david.musicsearch.data.coverart

import androidx.paging.cachedIn
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.data.coverart.api.toImageMetadataList
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import kotlin.time.Duration.Companion.seconds

internal class ImageMetadataRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
    private val coroutineScope: CoroutineScope,
) : ImageMetadataRepository {

    override suspend fun getAndSaveImageMetadata(
        mbid: String,
        entity: MusicBrainzEntity,
        forceRefresh: Boolean,
    ): ImageMetadata {
        if (forceRefresh) {
            imageUrlDao.deleteAllImageMetadtaById(mbid)
        }

        val cachedImageMetadata = imageUrlDao.getFrontImageMetadata(mbid)
        return if (cachedImageMetadata == null) {
            saveImageMetadataFromNetwork(mbid, entity)
            imageUrlDao.getFrontImageMetadata(mbid) ?: ImageMetadata()
        } else {
            cachedImageMetadata
        }
    }

    private suspend fun saveImageMetadataFromNetwork(
        mbid: String,
        entity: MusicBrainzEntity,
    ) {
        fetchImageMetadataFromNetwork(
            mbid = mbid,
            entity = entity,
        ) { imageMetadataList ->
            imageUrlDao.saveImageMetadata(
                mbid = mbid,
                imageMetadataList = imageMetadataList,
            )
        }
    }

    private suspend fun fetchImageMetadataFromNetwork(
        mbid: String,
        entity: MusicBrainzEntity,
        completion: (List<ImageMetadata>) -> Unit,
    ) {
        try {
            val coverArts: CoverArtsResponse = coverArtArchiveApi.getCoverArts(mbid, entity)
            val imageMetadataList: MutableList<ImageMetadata> = coverArts.toImageMetadataList().toMutableList()

            // We use an empty ImageUrls to represent that we've searched but failed to find any images.
            if (imageMetadataList.isEmpty()) {
                imageMetadataList.add(ImageMetadata())
            }

            completion(imageMetadataList)
        } catch (ex: HandledException) {
            if (ex.errorResolution == ErrorResolution.None) {
                completion(listOf(ImageMetadata()))
            } else {
                logger.e(ex)
            }
        } catch (ex: Exception) {
            logger.e(ex)
        }
    }

    private val mutex = Mutex()
    private var saveImageMetadataJob: Job? = null
    private var mbidToImageMetadataMap: MutableMap<String, List<ImageMetadata>> = mutableMapOf()
    private var lastSaved = Clock.System.now()
    private var maxWaitTime = 5.seconds
    private var maxBatchSize: Int = 100

    override suspend fun saveImageMetadata(
        mbid: String,
        entity: MusicBrainzEntity,
    ) {
        if (mbidToImageMetadataMap.isEmpty()) {
            lastSaved = Clock.System.now()
        }
        fetchImageMetadataFromNetwork(
            mbid = mbid,
            entity = entity,
        ) { imageMetadataList ->
            mbidToImageMetadataMap[mbid] = imageMetadataList
        }

        saveImageMetadataJob?.cancel()
        saveImageMetadataJob = coroutineScope.launch {
            val isReadyToSave = mbidToImageMetadataMap.size >= maxBatchSize ||
                Clock.System.now() - lastSaved >= maxWaitTime

            if (isReadyToSave) {
                batchSaveImageMetadata()
            } else {
                val timeToWait = maxWaitTime - (Clock.System.now() - lastSaved)
                delay(timeToWait)
                batchSaveImageMetadata()
            }
        }
    }

    private suspend fun batchSaveImageMetadata() {
        mutex.withLock {
            imageUrlDao.saveImageMetadata(
                mbidToImageMetadataMap = mbidToImageMetadataMap,
            )
            mbidToImageMetadataMap.clear()
            lastSaved = Clock.System.now()
        }
    }

    override fun getNumberOfImageMetadataById(mbid: String): Int {
        return imageUrlDao.getNumberOfImagesById(mbid).toInt()
    }

    override fun observeAllImageMetadata(
        mbid: String?,
        query: String,
        sortOption: ImagesSortOption,
    ): Flow<PagingData<ImageMetadata>> = Pager(
        config = PagingConfig(
            pageSize = 100,
            initialLoadSize = 100,
            prefetchDistance = 50,
        ),
        pagingSourceFactory = {
            if (mbid == null) {
                imageUrlDao.getAllImageMetadata(
                    query = query,
                    sortOption = sortOption,
                )
            } else {
                imageUrlDao.getAllImageMetadataById(
                    mbid = mbid,
                    query = query,
                )
            }
        },
    ).flow
        .distinctUntilChanged()
        .cachedIn(scope = coroutineScope)

    override fun observeCountOfAllImageMetadata(): Flow<Long> {
        return imageUrlDao.observeCountOfAllImageMetadata()
    }
}
