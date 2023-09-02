package ly.david.ui.common

import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.data.core.network.MusicBrainzEntity

/**
 * Contract with generic properties that each MusicBrainz scaffold ViewModel should implement.
 */
interface MusicBrainzEntityViewModel {
    val entity: MusicBrainzEntity

    val title: MutableStateFlow<String>
    val isError: MutableStateFlow<Boolean>

    fun setTitle(title: String?) {
        this.title.value = title ?: return
    }
}
