package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.domain.label.LabelRepository
import ly.david.ui.common.MusicBrainzResourceViewModel
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class LabelScaffoldViewModel @Inject constructor(
    private val repository: LabelRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.LABEL
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val label: MutableStateFlow<LabelListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun loadDataForTab(
        labelId: String,
        selectedTab: LabelTab
    ) {
        when (selectedTab) {
            LabelTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val labelListItemModel = repository.lookupLabel(labelId)
                        if (title.value.isEmpty()) {
                            title.value = labelListItemModel.getNameWithDisambiguation()
                        }
                        label.value = labelListItemModel
                        isError.value = false
                    } catch (ex: HttpException) {
                        Timber.e(ex)
                        isError.value = true
                    } catch (ex: IOException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = labelId,
                            resource = resource,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            LabelTab.RELATIONSHIPS -> loadRelations(labelId)
            else -> {
                // Not handled here.
            }
        }
    }
}
