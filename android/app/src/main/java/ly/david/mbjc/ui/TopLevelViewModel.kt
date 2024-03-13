package ly.david.mbjc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.ActionableResult
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUriPlural
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzLoginActivityResultContract
import ly.david.musicsearch.domain.collection.usecase.GetAllCollections
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class TopLevelViewModel(
    private val collectionDao: CollectionDao,
    private val getAllCollections: GetAllCollections,
    private val collectionEntityDao: CollectionEntityDao,
    private val musicBrainzApi: MusicBrainzApi,

    private val musicBrainzLoginActivityResultContract: MusicBrainzLoginActivityResultContract,
) : ViewModel() {

    private val entity: MutableStateFlow<MusicBrainzEntity> = MutableStateFlow(MusicBrainzEntity.ARTIST)
    private val entityId: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val collections: Flow<PagingData<CollectionListItemModel>> =
        entity.flatMapLatest {
            getAllCollections(entity = it)
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun setEntity(entity: MusicBrainzEntity) {
        this.entity.value = entity
    }

    fun setEntityId(entityId: String) {
        this.entityId.value = entityId
    }

    suspend fun addToCollectionAndGetResult(collectionId: String): ActionableResult {
        var result = ActionableResult()

        // TODO: handle this in repository
        val collection = collectionDao.getCollection(collectionId)
        if (collection.isRemote) {
            try {
                musicBrainzApi.uploadToCollection(
                    collectionId = collectionId,
                    resourceUriPlural = entity.value.resourceUriPlural,
                    mbids = entityId.value,
                )
            } catch (ex: RecoverableNetworkException) {
                val userFacingError = "Failed to add to ${collection.name}. Login has expired."
                Timber.e("$userFacingError $ex")
                return ActionableResult(
                    message = userFacingError,
                    actionLabel = "Login",
                )
            }
        }

        collectionEntityDao.withTransaction {
            val insertedOneEntry = collectionEntityDao.insert(
                collectionId = collectionId,
                entityId = entityId.value,
            )

            result = if (insertedOneEntry == INSERTION_FAILED_DUE_TO_CONFLICT) {
                ActionableResult("Already in ${collection.name}.")
            } else {
//                collectionDao.update(
//                    collection.copy(entityCount = collection.entityCount + 1)
//                )
                ActionableResult("Added to ${collection.name}.")
            }
        }

        return result
    }

    fun getLoginContract() = musicBrainzLoginActivityResultContract

    fun login(result: MusicBrainzLoginActivityResultContract.Result) {
        viewModelScope.launch {
            login(result)
        }
    }
}
